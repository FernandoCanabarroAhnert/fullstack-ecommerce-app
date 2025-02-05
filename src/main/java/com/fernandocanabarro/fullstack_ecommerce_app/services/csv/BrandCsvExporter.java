package com.fernandocanabarro.fullstack_ecommerce_app.services.csv;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

public class BrandCsvExporter {

    public static void export(HttpServletResponse response, List<BrandResponseDTO> brands) {
        String[] header = {"Id","Nome"};
        String[] attributeMapping = {"id","name"};
        ICsvBeanWriter csvBeanWriter;
        try {
            csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            csvBeanWriter.writeHeader(header);
            for (BrandResponseDTO brand : brands) {
                csvBeanWriter.write(brand, attributeMapping);
            }
            csvBeanWriter.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao exportar para CSV");
        }
    }
}
