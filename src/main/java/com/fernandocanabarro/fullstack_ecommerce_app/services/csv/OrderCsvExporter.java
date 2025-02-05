package com.fernandocanabarro.fullstack_ecommerce_app.services.csv;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

public class OrderCsvExporter {

    public static void export(HttpServletResponse response, List<OrderResponseDTO> orders) {
        String[] header = {"Id","E-mail","Data","Status","Valor Total","Desconto","Tipo de Pagamento"};
        String[] attributeMapping = {"id","userEmail","moment","status","totalValue","discountPercentage","paymentType"};
        ICsvBeanWriter csvBeanWriter;
        try {
            csvBeanWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
            csvBeanWriter.writeHeader(header);
            for (OrderResponseDTO order : orders) {
                csvBeanWriter.write(order, attributeMapping);
            }
            csvBeanWriter.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao gerar CSV");
        }
    }
}
