package com.fernandocanabarro.fullstack_ecommerce_app.services.csv;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter {

    public static void export(HttpServletResponse response, List<UserResponseDTO> users) {
        String[] header = {"Id","Nome Completo","E-mail","CPF","Data de Nascimento","Funções","Ativado"};
        String[] attributeMapping = {"id","fullName","email","cpf","birthDate","roles","activated"};
        ICsvBeanWriter csvBeanWriter;
        try {
            csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            csvBeanWriter.writeHeader(header);
            for (UserResponseDTO user : users) {
                csvBeanWriter.write(user, attributeMapping);
            }
            csvBeanWriter.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao exportar para CSV");
        }
    }
}
