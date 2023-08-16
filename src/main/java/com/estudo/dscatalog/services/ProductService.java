package com.estudo.dscatalog.services;

import com.estudo.dscatalog.DTO.ProductDTO;
import com.estudo.dscatalog.entities.Product;
import com.estudo.dscatalog.repositories.ProductRepository;
import com.estudo.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(PageRequest pageRequest){
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(prod -> new ProductDTO(prod));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product prod = obj.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductDTO(prod);
    }

    @Transactional
    public ProductDTO insert(ProductDTO objDTO){
        Product prod = convertDtoToEntity(objDTO);
        prod = repository.save(prod);
        return new ProductDTO(prod);
    }

    @Transactional
    public ProductDTO update(ProductDTO newObj, Long id) {
        try {
            Product entity = new Product();
            entity = repository.getReferenceById(id);
            updateData(entity, newObj);
            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Product id not found: id - "+ id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            Product prod = repository.getReferenceById(id);
            repository.delete(prod);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Entity not found for delete");
        }
    }

    private Product convertDtoToEntity(ProductDTO dto) {
        return new Product(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImgUrl(), dto.getDate());
    }

    private void updateData(Product entity, ProductDTO newObj) {
        entity.setName(newObj.getName());
        entity.setDescription(newObj.getDescription());
        entity.setPrice(newObj.getPrice());
        entity.setImgUrl(newObj.getImgUrl());
        entity.setDate(newObj.getDate());
    }

}
