package com.ecom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ecom.model.Category;
import com.ecom.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {

@Autowired
private CategoryService categoryService;

@GetMapping
public ResponseEntity<Map<String, Object>> getAllCategories(
@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
Page<Category> page = categoryService.getAllCategorPagination(pageNo, pageSize);
List<Category> categorys = page.getContent();
Map<String, Object> response = new HashMap<>();
response.put("categorys", categorys);
response.put("pageNo", page.getNumber());
response.put("pageSize", pageSize);
response.put("totalElements", page.getTotalElements());
response.put("totalPages", page.getTotalPages());
response.put("isFirst", page.isFirst());
response.put("isLast", page.isLast());
return new ResponseEntity<>(response, HttpStatus.OK);
}
}