package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.OrderService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.csv.OrderCsvExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.excel.OrderExcelExporter;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.impl.JasperService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JasperService jasperService;

    @GetMapping("/orders-management")
    public String ordersManagement(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderResponseDTO> pageResponse = orderService.adminFindAllOrdersPaginated(pageable, "", "");

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();
        String sort = "id";
        String sortDirection = "asc";

        model.addAttribute("orders", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("minDate", "");
        model.addAttribute("maxDate", "");

        return "admin/orders/orders-management";
    }

    @GetMapping("/orders-management/paginated")
    public String ordersManagementPaginated(Model model,@RequestParam String page,
                                            @RequestParam String sort,
                                            @RequestParam String sortDirection,
                                            @RequestParam(defaultValue = "") String minDate,
                                            @RequestParam(defaultValue = "") String maxDate) {


        Pageable pageable = sortDirection.equals("asc")
            ? PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).ascending())
            : PageRequest.of(Integer.parseInt(page) - 1, 10).withSort(Sort.by(sort).descending());

        Page<OrderResponseDTO> pageResponse = orderService.adminFindAllOrdersPaginated(pageable, minDate, maxDate);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = Integer.parseInt(page);
        int totalPages = pageResponse.getTotalPages();

        model.addAttribute("orders", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("minDate", minDate);
        model.addAttribute("maxDate", maxDate);

        return "admin/orders/orders-management";
    }

    @GetMapping("/orders/excel/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm"));
        String fileName = "pedidos_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<OrderResponseDTO> orders = orderService.adminFindAll();
        OrderExcelExporter orderExcelExporter = new OrderExcelExporter(orders);
        orderExcelExporter.export(response);
    }

    @GetMapping("/orders/csv/export")
    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm"));
        String fileName = "pedidos_" + currentDateTime + ".csv";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        List<OrderResponseDTO> orders = orderService.adminFindAll();
        OrderCsvExporter.export(response, orders);
    }

    @GetMapping("/orders/pdf/export")
    public void exportToPdf(HttpServletResponse response,
                            @RequestParam(name = "minDate",required = false) String minDate,
                            @RequestParam(name = "maxDate",required = false) String maxDate,
                            @RequestParam(name = "minValue",required = false) BigDecimal minValue,
                            @RequestParam(name = "maxValue",required = false) BigDecimal maxValue,
                            @RequestParam(name = "cartao",required = false) String cartao,
                            @RequestParam(name = "boleto",required = false) String boleto,
                            @RequestParam(name = "pix",required = false) String pix,
                            @RequestParam(name = "waitingPayment",required = false) String waitingPayment,
                            @RequestParam(name = "paid",required = false) String paid,
                            @RequestParam(name = "shipped",required = false) String shipped,
                            @RequestParam(name = "delivered",required = false) String delivered,
                            @RequestParam(name = "canceled",required = false) String canceled
                            ) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm"));
        String fileName = "pedidos_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date min, max;

        try {
            min = !minDate.equals("") ? setBeginOfDay(sdf.parse(minDate)) : getOneYearAgo();
            max = !maxDate.equals("") ? setEndOfDay(sdf.parse(maxDate)) : setEndOfDay(new Date());
        }
        catch (ParseException e) {
            throw new BadRequestException("Erro ao gerar PDF");
        }

        jasperService.addParams("MIN_DATE", min);
        jasperService.addParams("MAX_DATE", max);
        jasperService.addParams("MIN_VALUE", minValue);
        jasperService.addParams("MAX_VALUE", maxValue);
        jasperService.addParams("CARTAO", cartao);
        jasperService.addParams("BOLETO", boleto);
        jasperService.addParams("PIX", pix);
        jasperService.addParams("WAITING_PAYMENT", waitingPayment);
        jasperService.addParams("PAID", paid);
        jasperService.addParams("SHIPPED", shipped);
        jasperService.addParams("DELIVERED", delivered);
        jasperService.addParams("CANCELED", canceled);
        jasperService.exportToPdf(response, JasperService.ORDERS);
    }

    private Date getOneYearAgo() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date setBeginOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date setEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
