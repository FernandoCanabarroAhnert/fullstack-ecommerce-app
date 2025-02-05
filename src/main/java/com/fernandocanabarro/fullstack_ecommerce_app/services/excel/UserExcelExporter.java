package com.fernandocanabarro.fullstack_ecommerce_app.services.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<UserResponseDTO> users;

    public UserExcelExporter(List<UserResponseDTO> users) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Usuários");
        this.users = users;
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        Cell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Nome Completo");
        cell.setCellStyle(style);
        
        cell = row.createCell(2);
        cell.setCellValue("E-mail");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("CPF");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Data de Nascimento");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("Funções");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("Ativado");
        cell.setCellStyle(style);
    }

    private void writeDataRows() {
        int rowCount = 1;
        for (UserResponseDTO user : users) {
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(user.getId());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(user.getFullName());
            sheet.autoSizeColumn(1);

            cell = row.createCell(2);
            cell.setCellValue(user.getEmail());
            sheet.autoSizeColumn(2);

            cell = row.createCell(3);
            cell.setCellValue(user.getCpf());
            sheet.autoSizeColumn(3);

            cell = row.createCell(4);
            cell.setCellValue(user.getBirthDate());
            sheet.autoSizeColumn(4);

            cell = row.createCell(5);
            cell.setCellValue(user.getRoles());
            sheet.autoSizeColumn(5);

            cell = row.createCell(6);
            cell.setCellValue(user.getActivated() ? "true" : "false");
            sheet.autoSizeColumn(6);
        };
    }

    public void export(HttpServletResponse response) {
        try {
            writeHeaderRow();
            writeDataRows();
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao exportar Excel");
        }
    }
}
