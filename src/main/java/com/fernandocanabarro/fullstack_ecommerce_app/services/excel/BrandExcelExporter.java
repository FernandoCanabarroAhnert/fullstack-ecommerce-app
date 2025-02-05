package com.fernandocanabarro.fullstack_ecommerce_app.services.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class BrandExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<BrandResponseDTO> brands;

    public BrandExcelExporter(List<BrandResponseDTO> brands) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Marcas");
        this.brands = brands;
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
        cell.setCellValue("Nome");
        cell.setCellStyle(style);
    }

    private void writeDataRows() {
        int rowCount = 1;
        for (BrandResponseDTO brand : brands) {
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(brand.getId());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(brand.getName());
            sheet.autoSizeColumn(1);
        }
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
