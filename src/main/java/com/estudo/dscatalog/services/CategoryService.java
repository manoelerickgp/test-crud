package com.estudo.dscatalog.services;

import com.estudo.dscatalog.DTO.CategoryDTO;
import com.estudo.dscatalog.entities.Category;
import com.estudo.dscatalog.repositories.CategoryRepository;
import com.estudo.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(PageRequest pageRequest){
        Page<Category> list = repository.findAll(pageRequest);
        return list.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO objDTO) {
        Category entity = convertDtoToEntity(objDTO);
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO newDTO) {
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(newDTO.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Category id not found: " + id);
        }
    }

    @Transactional
    public void delete(Long id){
        try {
            Category obj = repository.getReferenceById(id);
            repository.delete(obj);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Entity not found for delete");
        }
    }

    private Category convertDtoToEntity(CategoryDTO objDTO) {
        return new Category(objDTO.getName());
    }

    private void updateData(Category referenceEntity, CategoryDTO newObj) {
        referenceEntity.setName(newObj.getName());
    }


}
