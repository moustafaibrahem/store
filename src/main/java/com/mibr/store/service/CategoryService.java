package com.mibr.store.service;

import com.mibr.store.data.category.Category;
import com.mibr.store.data.category.CategoryRepository;
import com.mibr.store.data.history.History;
import com.mibr.store.data.history.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Locale;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private MessageSource messageSource;


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
    @Transactional
    public void addOrDeleteQuantity(Long categoryId, int quantity, String status, Locale locale) throws IllegalArgumentException {
        if (quantity <= 0) {
            String errorMessage = messageSource.getMessage("error.invalidQuantity", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        if ("Delete".equals(status)) {
            if (category.getQuantity() < quantity) {
                String errorMessage = messageSource.getMessage("error.insufficientQuantity", null, locale);
                throw new IllegalArgumentException(errorMessage);
            }
            category.setQuantity(category.getQuantity() - quantity);
        } else if ("Add".equals(status)) {
            category.setQuantity(category.getQuantity() + quantity);
        }

        categoryRepository.save(category);

        // Save to history
        History history = new History();
        history.setCategory(category);
        history.setQuantity(quantity);
        history.setStatus(status);
        historyRepository.save(history);
    }

    // Method to retrieve all categories
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    // New method to find categories by name
    public List<Category> findCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}
