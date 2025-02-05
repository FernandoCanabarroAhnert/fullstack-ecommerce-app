package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class JasperService {

    @Autowired
    private Connection connection;

    public static String ORDER_SUMMARY = "order-summary";
    public static String PRODUCTS = "products";
    public static String USERS = "users";
    public static String BRANDS = "brands";
    public static String CATEGORIES = "categories";
    public static String COUPONS = "coupons";
    public static String ORDERS = "orders";
    public static String BOLETO = "boleto";

    public JasperService() {
        params.put("IMAGES_DIR", getClass().getClassLoader().getResource("jasper/").toString());
        params.put("SUB_REPORT_DIR", getClass().getClassLoader().getResource("jasper/").toString());
    }

    private Map<String,Object> params = new HashMap<>();

    public void addParams(String key, Object value) {
        params.put(key, value);
    }

    public void exportToPdf(HttpServletResponse response,String fileName) {
        try {
            byte[] bytes;
            InputStream jasperStream = getClass().getClassLoader().getResourceAsStream("jasper/" + fileName + ".jasper");

            if (jasperStream == null) {
                throw new FileNotFoundException("Arquivo Jasper não encontrado: " + fileName);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, connection);
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            response.getOutputStream().write(bytes);
        }
        catch (IOException | JRException e) {
            throw new BadRequestException("Erro ao gerar PDF");
        }
    }
}
