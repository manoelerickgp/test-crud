package com.estudo.dscatalog.services;

import com.estudo.dscatalog.DTO.CategoryDTO;
import com.estudo.dscatalog.DTO.ProductDTO;
import com.estudo.dscatalog.entities.Category;
import com.estudo.dscatalog.entities.Product;
import com.estudo.dscatalog.repositories.CategoryRepository;
import com.estudo.dscatalog.repositories.ProductRepository;
import com.estudo.dscatalog.services.exceptions.DatabaseException;
import com.estudo.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(PageRequest pageRequest){
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(prod -> new ProductDTO(prod, prod.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product prod = obj.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductDTO(prod, prod.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO newObj){
        Product entity = new Product();
        convertDtoToEntity(entity, newObj);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(ProductDTO newObj, Long id) {
        try {
            Product entity = repository.getReferenceById(id);
            convertDtoToEntity(entity, newObj);
            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Product id not found: id - "+ id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found "+ id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void convertDtoToEntity(Product entity, ProductDTO newObj) {
        entity.setName(newObj.getName());
        entity.setDescription(newObj.getDescription());
        entity.setPrice(newObj.getPrice());
        entity.setImgUrl(newObj.getImgUrl());
        entity.setDate(newObj.getDate());

        entity.getCategories().clear();
        for (CategoryDTO catDTO : newObj.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDTO.getId());
            entity.getCategories().add(category);
        }
    }

}
