package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.io.IOException;
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

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductImageDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductReportDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.BrandService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CategoryService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.ProductService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.csv.ProductCsvExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.excel.ProductExcelExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private JasperService jasperService;

    @GetMapping("/products-management")
    public String productsManagement(Model model){
        Pageable pageable = PageRequest.of(0, 10);
        String name = "";
        Page<ProductReportDTO> page = productService.findAllForReportPaginated(name,pageable);

        int totalPageItems = page.getNumberOfElements();
        long totalItems = page.getTotalElements();
        int currentPage = 1;
        int totalPages = page.getTotalPages();

        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", page.getContent());
        model.addAttribute("sort", "id");
        model.addAttribute("sortDirection","asc");
        model.addAttribute("name", name);
        return "admin/products/products-management";
    }

    @GetMapping("/products-management/paginated")
    public String productsManagementPaginated(@RequestParam String page,
                                            @RequestParam String sort,
                                            @RequestParam String sortDirection,
                                            @RequestParam String name,
                                            Model model){
        Pageable pageable = sortDirection.equals("asc") 
            ? PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).ascending())
            : PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).descending());

        Page<ProductReportDTO> pageResponse = productService.findAllForReportPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = Integer.parseInt(page);
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", pageResponse.getContent());
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("name", name);
        return "admin/products/products-management";
    }

    @GetMapping("/products/create")
    public String createProductForm(Model model){
        List<CategoryResponseDTO> categories = categoryService.findAll();
        List<BrandResponseDTO> brands = brandService.findAll();
        ProductRequestDTO productDto = new ProductRequestDTO();
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "admin/products/create-product-form";
    }

    @PostMapping("/products")
    public String createProduct(@Valid @ModelAttribute("productDto") ProductRequestDTO productDto, BindingResult bindingResult,Model model) throws IOException{
        if (bindingResult.hasErrors()) {
            List<CategoryResponseDTO> categories = categoryService.findAll();
            List<BrandResponseDTO> brands = brandService.findAll();
            model.addAttribute("productDto", productDto);
            model.addAttribute("categories", categories);
            model.addAttribute("brands", brands);
            return "admin/products/create-product-form";
        }
        productService.createProduct(productDto);
        return "redirect:/admin/products-management";
    }

    @GetMapping("/products/{id}/update")
    public String updateProductForm(@PathVariable Long id, Model model){
        List<CategoryResponseDTO> categories = categoryService.findAll();
        List<BrandResponseDTO> brands = brandService.findAll();
        ProductRequestDTO productDto = productService.findProductByIdToUpdate(id);
        List<ProductImageDTO> images = productService.findProductImagesByProductId(id);

        model.addAttribute("id", id);
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("images", images);
        return "admin/products/update-product-form";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("productDto") ProductRequestDTO productDto, BindingResult bindingResult,Model model) throws IOException{
        if (bindingResult.hasErrors()) {
            List<CategoryResponseDTO> categories = categoryService.findAll();
            List<BrandResponseDTO> brands = brandService.findAll();
            model.addAttribute("productDto", productDto);
            model.addAttribute("categories", categories);
            model.addAttribute("brands", brands);
            return "admin/products/update-product-form";
        }
        productService.updateProduct(id,productDto);
        return "redirect:/admin/products-management";
    }

    @GetMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/products-management";
    }

    @GetMapping("/products/{id}/increase")
    public String increaseProductStockQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.increaseProductStockQuantity(id, quantity);
        return "redirect:/admin/products-management";
    }

    @GetMapping("/products/{id}/decrease")
    public String decreaseProductStockQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.decreaseProductStockQuantity(id, quantity);
        return "redirect:/admin/products-management";
    }

    @GetMapping("/products/{id}/delete-image/{imageId}")
    public String deleteProductImage(@PathVariable Long id, @PathVariable Long imageId) {
        productService.deleteProductImageById(imageId);
        return "redirect:/admin/products/" + id + "/update";
    }

    @GetMapping("/products/excel/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "produtos_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<ProductReportDTO> products = productService.findAllForReport();
        ProductExcelExporter productExcelExporter = new ProductExcelExporter(products);
        productExcelExporter.export(response);
    }

    @GetMapping("/products/csv/export")
    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("ISO-8859-1");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "produtos_" + currentDateTime + ".csv";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<ProductReportDTO> products = productService.findAllForReport();
        ProductCsvExporter.export(response, products);
    }

    @GetMapping("/products/pdf/export")
    public void exportToPdf(HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "produtos_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        jasperService.exportToPdf(response, JasperService.PRODUCTS);
    }
}
