package com.fernandocanabarro.fullstack_ecommerce_app.services.csv;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductReportDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

public class ProductCsvExporter {

    public static void export(HttpServletResponse response, List<ProductReportDTO> products) {
        String[] csvHeader = {"ID","Nome","Descrição","Preço","Marca","Estoque","Categorias"};
        String[] attributeMapping = {"id","name","description","price","brand","stockQuantity","categories"};
        ICsvBeanWriter csvBeanWriter;
        try {
            csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            csvBeanWriter.writeHeader(csvHeader);
            for (ProductReportDTO product : products) {
                csvBeanWriter.write(product, attributeMapping);
            }
            csvBeanWriter.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao exportar para CSV");
        }
    }
}
