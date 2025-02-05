package com.fernandocanabarro.fullstack_ecommerce_app.services.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductReportDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ProductExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ProductReportDTO> products;

    public ProductExcelExporter(List<ProductReportDTO> products) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Produtos");
        this.products = products;
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

        cell = row.createCell(2);
        cell.setCellValue("Descrição");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Preço");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Marca");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("Estoque");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("Categorias");
        cell.setCellStyle(style);
    }

    private void writeDataRows() {
        int rowCount = 1;
        for (ProductReportDTO product : products) {
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(product.getId());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(product.getName());
            sheet.autoSizeColumn(1);

            cell = row.createCell(2);
            cell.setCellValue(product.getDescription());
            sheet.autoSizeColumn(2);

            cell = row.createCell(3);
            cell.setCellValue(product.getPrice());
            sheet.autoSizeColumn(3);

            cell = row.createCell(4);
            cell.setCellValue(product.getBrand());
            sheet.autoSizeColumn(4);

            cell = row.createCell(5);
            cell.setCellValue(product.getStockQuantity());
            sheet.autoSizeColumn(5);

            cell = row.createCell(6);
            cell.setCellValue(product.getCategories());
            sheet.autoSizeColumn(6);
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
