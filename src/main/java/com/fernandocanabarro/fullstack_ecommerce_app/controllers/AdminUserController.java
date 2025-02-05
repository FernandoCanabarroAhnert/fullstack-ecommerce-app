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

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.RoleDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.csv.UserCsvExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.excel.UserExcelExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    
    @Autowired
    private AuthService authService;
    @Autowired
    private JasperService jasperService;

    @GetMapping("/users-management")
    public String usersManagement(Model model) {
        Pageable pageable = PageRequest.of(0,10);
        String name = "";
        Page<UserResponseDTO> pageResponse = authService.adminFindAllUsersPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();
        String sort = "id";
        String sortDirection = "asc";

        model.addAttribute("users", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("name", name);

        return "admin/users/users-management";
    }

    @GetMapping("/users-management/paginated")
    public String usersManagementPaginated(Model model,
            @RequestParam String page,
            @RequestParam String sort,
            @RequestParam String name,
            @RequestParam String sortDirection) {
        Pageable pageable = sortDirection.equals("asc")
            ? PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).ascending())
            : PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).descending());
        Page<UserResponseDTO> pageResponse = authService.adminFindAllUsersPaginated(name,pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = Integer.parseInt(page);
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("users", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("name", name);

        return "admin/users/users-management";
    }

    @GetMapping("/users/create")
    public String createUserForm(Model model) {
        UserRequestDTO userDto = new UserRequestDTO();
        List<RoleDTO> roles = authService.findAllRoles();
        model.addAttribute("userDto", userDto);
        model.addAttribute("roles", roles);
        return "admin/users/create-user-form";
    }

    @PostMapping("/users")
    public String createUser(@Valid @ModelAttribute("userDto") UserRequestDTO userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = authService.findAllRoles();
            model.addAttribute("userDto", userDto);
            model.addAttribute("roles", roles);
            return "admin/users/create-user-form";
        }
        authService.adminCreateUser(userDto);
        return "redirect:/admin/users-management";
    }

    @GetMapping("/users/{id}/update")
    public String updateUserForm(@PathVariable Long id, Model model) {
        UserUpdateDTO userDto = authService.findByIdToUpdate(id);
        List<RoleDTO> roles = authService.findAllRoles();
        model.addAttribute("userDto", userDto);
        model.addAttribute("roles", roles);
        return "admin/users/update-user-form";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("userDto") UserUpdateDTO userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = authService.findAllRoles();
            model.addAttribute("id", id);
            model.addAttribute("userDto", userDto);
            model.addAttribute("roles", roles);
            return "admin/users/update-user-form";
        }
        authService.adminUpdateUser(id, userDto);
        return "redirect:/admin/users-management";
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return "redirect:/admin/users-management";
    }

    @GetMapping("/users/excel/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "usuarios_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<UserResponseDTO> users = authService.adminFindAll();
        UserExcelExporter userExcelExporter = new UserExcelExporter(users);
        userExcelExporter.export(response);
    }

    @GetMapping("/users/csv/export")
    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("ISO-8859-1");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "usuarios_" + currentDateTime + ".csv";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<UserResponseDTO> users = authService.adminFindAll();
        UserCsvExporter.export(response, users);
    }

    @GetMapping("/users/pdf/export")
    public void exportToPdf(HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "usuarios_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        jasperService.exportToPdf(response, JasperService.USERS);
    }
}
