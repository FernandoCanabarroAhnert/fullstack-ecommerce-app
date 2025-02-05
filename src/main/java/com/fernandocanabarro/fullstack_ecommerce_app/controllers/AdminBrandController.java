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

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.BrandService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.csv.BrandCsvExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.excel.BrandExcelExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminBrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private JasperService jasperService;

    @GetMapping("/brands-management")
    public String brandsManagement(Model model){
        Pageable pageable = PageRequest.of(0, 10);
        String name = "";
        Page<BrandResponseDTO> pageResponse = brandService.findAllPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("brands", pageResponse.getContent());
        model.addAttribute("sort", "id");
        model.addAttribute("sortDirection","asc");
        model.addAttribute("name", name);
        return "admin/brands/brands-management";
    }

    @GetMapping("/brands-management/paginated")
    public String brandsManagementPaginated(@RequestParam String page,
                                            @RequestParam String sort,
                                            @RequestParam String sortDirection,
                                            @RequestParam String name,
                                            Model model){
        Pageable pageable = sortDirection.equals("asc") 
            ? PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).ascending())
            : PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).descending());

        Page<BrandResponseDTO> pageResponse = brandService.findAllPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = Integer.parseInt(page);
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("brands", pageResponse.getContent());
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("name", name);
        return "admin/brands/brands-management";
    }

     @GetMapping("/brands/create")
    public String createBrandForm(Model model) {
        BrandRequestDTO brandRequestDTO = new BrandRequestDTO();
        model.addAttribute("brandDto", brandRequestDTO);
        return "admin/brands/create-brand-form";
    }

    @PostMapping("/brands")
    public String createBrand(@Valid @ModelAttribute("brandDto") BrandRequestDTO brandDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brandDto", brandDto);
            return "admin/brands/create-brand-form";
        }
        brandService.addBrand(brandDto);
        return "redirect:/admin/brands-management";
    }

    @GetMapping("/brands/{id}/update")
    public String updateBrandForm(@PathVariable Long id ,Model model) {
        BrandRequestDTO brandDto = brandService.findByIdToUpdate(id);
        model.addAttribute("id", id);
        model.addAttribute("brandDto", brandDto);
        return "admin/brands/update-brand-form";
    }

    @PostMapping("/brands/{id}")
    public String updateBrand(@PathVariable Long id, @Valid @ModelAttribute("brandDto") BrandRequestDTO brandDto, BindingResult bindingResult,
        Model model) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("brandDto", brandDto);
                return "admin/brands/update-brand-form";
            }
            brandService.updateBrand(id, brandDto);
            return "redirect:/admin/brands-management";
    }

    @GetMapping("/brands/{id}/delete")
    public String deleteBrand(@PathVariable Long id, Model model) {
        brandService.deleteBrand(id);
        return "redirect:/admin/brands-management";
    }

    @GetMapping("/brands/excel/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "marcas_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<BrandResponseDTO> brands = brandService.findAll();
        BrandExcelExporter brandExcelExporter = new BrandExcelExporter(brands);
        brandExcelExporter.export(response);
    }

    @GetMapping("/brands/csv/export")
    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("ISO-8859-1");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "marcas_" + currentDateTime + ".csv";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<BrandResponseDTO> brands = brandService.findAll();
        BrandCsvExporter.export(response, brands);
    }

    @GetMapping("/brands/pdf/export")
    public void exportToPdf(HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "marcas_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        jasperService.exportToPdf(response, JasperService.BRANDS);
    }
}
