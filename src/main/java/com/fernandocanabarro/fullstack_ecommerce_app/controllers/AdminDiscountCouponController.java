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

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.DiscountCouponService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.csv.DiscountCouponCsvExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.excel.DiscountCouponExcelExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminDiscountCouponController {

    @Autowired
    private DiscountCouponService discountCouponService;
    @Autowired
    private JasperService jasperService;

    @GetMapping("/coupons-management")
    public String cuponsManagement(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DiscountCouponDTO> pageResponse = discountCouponService.adminFindAllPaginated("",pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();
        String sort = "id";
        String sortDirection = "asc";
        String description = "";

        model.addAttribute("coupons", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("description", description);

        return "admin/coupons/coupons-management";
    }

    @GetMapping("/coupons-management/paginated")
    public String cuponsManagementPaginated(@RequestParam String page,
                                            @RequestParam String sort,
                                            @RequestParam String sortDirection,
                                            @RequestParam String description,
                                            Model model) {
        Pageable pageable = sortDirection.equals("asc")
            ? PageRequest.of(0, 10).withSort(Sort.by(sort).ascending())
            : PageRequest.of(0, 10).withSort(Sort.by(sort).descending());
        Page<DiscountCouponDTO> pageResponse = discountCouponService.adminFindAllPaginated(description,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("coupons", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("description", description);

        return "admin/coupons/coupons-management";
    }

    @GetMapping("/coupons/create")
    public String createDiscountCouponForm(Model model) {
        DiscountCouponDTO couponDto = new DiscountCouponDTO();
        model.addAttribute("couponDto", couponDto);
        return "admin/coupons/create-coupon-form";
    }

    @PostMapping("/coupons")
    public String createDiscountCoupon(@Valid @ModelAttribute("couponDto") DiscountCouponDTO couponDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("couponDto", couponDto);
            return "admin/coupons/create-coupon-form";
        }
        discountCouponService.adminCreateDiscountCoupon(couponDto);
        return "redirect:/admin/coupons-management";
    }

    @GetMapping("/coupons/{id}/update")
    public String updateDiscountCouponForm(@PathVariable Long id, Model model) {
        DiscountCouponDTO couponDto = discountCouponService.adminFindByIdToUpdate(id);
        model.addAttribute("couponDto", couponDto);
        return "admin/coupons/update-coupon-form";
    }

    @PostMapping("/coupons/{id}")
    public String updateDiscountCoupon(@Valid @ModelAttribute("couponDto") DiscountCouponDTO couponDto, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("couponDto", couponDto);
            return "admin/coupons/update-coupon-form";
        }
        discountCouponService.adminUpdateDiscountCoupon(id, couponDto);
        return "redirect:/admin/coupons-management";
    }

    @GetMapping("/coupons/excel/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "cupons_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<DiscountCouponDTO> coupons = discountCouponService.adminFindAll();
        DiscountCouponExcelExporter discountCouponExcelExporter = new DiscountCouponExcelExporter(coupons);
        discountCouponExcelExporter.export(response);
    }

    @GetMapping("/coupons/csv/export")
    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("ISO-8859-1");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "cupons_" + currentDateTime + ".csv";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<DiscountCouponDTO> coupons = discountCouponService.adminFindAll();
        DiscountCouponCsvExporter.export(response, coupons);
    }

    @GetMapping("/coupons/pdf/export")
    public void exportToPdf(HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "cupons_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        jasperService.exportToPdf(response, JasperService.COUPONS);
    }
}
