package com.estudo.dscatalog.resources;

import com.estudo.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> findAll(){
        List<Category> list = new ArrayList<>();
        list.add(new Category(1L, "Eletronics"));
        list.add(new Category(2L, "Books"));
        return ResponseEntity.ok().body(list);
    }
}
