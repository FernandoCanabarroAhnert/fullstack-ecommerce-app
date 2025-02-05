package com.fernandocanabarro.fullstack_ecommerce_app.services.csv;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

public class DiscountCouponCsvExporter {

    public static void export(HttpServletResponse response, List<DiscountCouponDTO> coupons) {
        String[] header = {"Id","Descrição","Código","Desconto","Dias Disponíveis"};
        String[] attributeMapping = {"id","description","code","discountPercentage","availableDays"};
        ICsvBeanWriter csvBeanWriter;
        try {
            csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            csvBeanWriter.writeHeader(header);
            for (DiscountCouponDTO coupon : coupons) {
                csvBeanWriter.write(coupon, attributeMapping);
            }
            csvBeanWriter.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao exportar para CSV");
        }
    }
}
