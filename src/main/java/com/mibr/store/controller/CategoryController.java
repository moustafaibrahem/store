package com.mibr.store.controller;

import com.mibr.store.data.category.Category;
import com.mibr.store.data.history.History;
import com.mibr.store.service.CategoryService;
import com.mibr.store.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;


@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HistoryService historyService;
    @Autowired
    private MessageSource messageSource;


    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/{id}")
    public String showCategoryHistory(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            model.addAttribute("history", historyService.getHistoryByCategoryId(id));
            return "category_history";
        } else {
            return "redirect:/categories";
        }
    }

/*
    @PostMapping("/{id}/addHistory")
    public String addHistory(@PathVariable Long id, @RequestParam int quantity, @RequestParam String status) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            Category cat = category.get();
            History history = new History();
            history.setCategory(cat);
            history.setCategoryName(cat.getName());
            history.setQuantity(quantity);
            history.setStatus(status);
            historyService.addHistoryRecord(history);
            // Update category quantity based on status
            if (status.equals("Add")) {
                cat.setQuantity(cat.getQuantity() + quantity);
            } else if (status.equals("Delete")) {
                cat.setQuantity(cat.getQuantity() - quantity);
            }
            categoryService.saveCategory(cat); // Save the updated category
            return "redirect:/categories/" + id;
        } else {
            return "redirect:/categories";
        }
    }
*/
@PostMapping("/{id}/addHistory")
public String addHistory(@PathVariable("id") Long id,
                         @RequestParam("quantity") int quantity,
                         @RequestParam("status") String status,
                         Locale locale,
                         RedirectAttributes redirectAttributes) {
    try {
        categoryService.addOrDeleteQuantity(id, quantity, status, locale);
        String successMessage = messageSource.getMessage("success.actionPerformed", null, locale);
        redirectAttributes.addFlashAttribute("message", successMessage);
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/categories/" + id;
}

    @PostMapping("/add")
    public String addCategory(@RequestParam String name, @RequestParam int quantity) {
        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setQuantity(quantity);
        categoryService.saveCategory(newCategory);
        return "redirect:/categories"; // Redirect to the list of categories after adding
    }

}
