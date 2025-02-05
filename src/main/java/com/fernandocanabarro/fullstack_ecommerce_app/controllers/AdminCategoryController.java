package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryUpdateDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CategoryService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.csv.CategoryCsvExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.excel.CategoryExcelExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JasperService jasperService;

    @GetMapping("/categories-management")
    public String categoriesManagement(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        String name = "";
        Page<CategoryResponseDTO> pageResponse = categoryService.findAllPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();
        String sort = "id";
        String sortDirection = "asc";

        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("categories", pageResponse.getContent());
        model.addAttribute("name", name);

        return "admin/categories/categories-management";
    }

    @GetMapping("/categories-management/paginated")
    public String categoriesManagementPaginated(Model model,
                    @RequestParam String page,
                    @RequestParam String sort,
                    @RequestParam String sortDirection,
                    @RequestParam String name) {
        Pageable pageable = sortDirection.equals("asc")
            ? PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).ascending())
            : PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).descending());

        Page<CategoryResponseDTO> pageResponse = categoryService.findAllPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = Integer.parseInt(page);
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("categories", pageResponse.getContent());
        model.addAttribute("name", name);

        return "admin/categories/categories-management";
    }

    @GetMapping("/categories/create")
    public String createCategoryForm(Model model) {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        model.addAttribute("categoryDto", categoryRequestDTO);
        return "admin/categories/create-category-form";
    }

    @PostMapping("/categories")
    public String createCategory(@Valid @ModelAttribute("categoryDto") CategoryRequestDTO categoryDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "admin/categories/create-category-form";
        }
        categoryService.createCategory(categoryDto);
        return "redirect:/admin/categories-management";
    }

    @GetMapping("/categories/{id}/update")
    public String updateCategoryForm(@PathVariable Long id ,Model model) {
        CategoryUpdateDTO categoryDto = categoryService.findByIdToUpdate(id);
        model.addAttribute("categoryDto", categoryDto);
        return "admin/categories/update-category-form";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute("categoryDto") CategoryUpdateDTO categoryDto, BindingResult bindingResult,
        Model model) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("categoryDto", categoryDto);
                return "admin/categories/update-category-form";
            }
            categoryService.updateCategory(id, categoryDto);
            return "redirect:/admin/categories-management";
    }

    @GetMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, Model model) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories-management";
    }

    @GetMapping("/categories/excel/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "categorias_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<CategoryResponseDTO> categories = categoryService.findAll();
        CategoryExcelExporter categoryExcelExporter = new CategoryExcelExporter(categories);
        categoryExcelExporter.export(response);
    }

    @GetMapping("/categories/csv/export")
    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("ISO-8859-1");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "categorias_" + currentDateTime + ".csv";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<CategoryResponseDTO> categories = categoryService.findAll();
        CategoryCsvExporter.export(response, categories);
    }

    @GetMapping("/categories/pdf/export")
    public void exportToPdf(HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "categorias_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        jasperService.exportToPdf(response, JasperService.CATEGORIES);
    }
}
