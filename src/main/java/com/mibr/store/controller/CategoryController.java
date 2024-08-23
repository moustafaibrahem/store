package com.mibr.store.controller;

import com.mibr.store.data.category.Category;
import com.mibr.store.service.CategoryService;
import com.mibr.store.util.NumberConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MessageSource messageSource;


//    @GetMapping
//    public String listCategories(Model model) {
//        model.addAttribute("categories", categoryService.getAllCategories());
//        return "categories";
//    }
    @GetMapping("")
    public String getFilteredCategories(@RequestParam(required = false) String name, Model model) {
        List<Category> categories;
        if (name != null && !name.isEmpty()) {
            categories = categoryService.findCategoriesByName(name);
        } else {
            categories = categoryService.findAllCategories();
        }
        model.addAttribute("categories", categories);
        return "categories"; // This should match the Thymeleaf template name
    }

    @GetMapping("/{id}")
    public String getCategoryHistory(@PathVariable Long id, Model model, Locale locale) {
        Category category = categoryService.findById(id);
        ZoneId egyptZone = ZoneId.of("Africa/Cairo");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss", locale).withZone(egyptZone);

        List<HistoryDto> history = category.getHistory().stream()
                .map(h -> new HistoryDto(h.getStatus(), h.getQuantity(), formatDate(h.getDate().atZone(egyptZone), formatter, locale)))
                .collect(Collectors.toList());

        model.addAttribute("category", category);
        model.addAttribute("history", history);
        return "category_history";
    }

    private String formatDate(ZonedDateTime dateTime, DateTimeFormatter formatter, Locale locale) {
        String formattedDate = dateTime.format(formatter);
        if (locale.getLanguage().equals(new Locale("ar").getLanguage())) {
            return NumberConverter.convertToArabicNumerals(formattedDate);
        }
        return formattedDate;
    }

    public static class HistoryDto {
        private String status;
        private int quantity;
        private String formattedDate;

        public HistoryDto(String status, int quantity, String formattedDate) {
            this.status = status;
            this.quantity = quantity;
            this.formattedDate = formattedDate;
        }

        public String getStatus() {
            return status;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getFormattedDate() {
            return formattedDate;
        }
    }

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
