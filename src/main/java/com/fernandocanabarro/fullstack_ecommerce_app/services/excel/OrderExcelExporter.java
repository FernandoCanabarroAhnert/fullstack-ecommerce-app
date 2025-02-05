package com.fernandocanabarro.fullstack_ecommerce_app.services.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;

public class OrderExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<OrderResponseDTO> orders;

    public OrderExcelExporter(List<OrderResponseDTO> orders) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        this.orders = orders;
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
        cell.setCellValue("E-mail");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Data");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Status");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Valor Total");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("Desconto");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("Tipo de Pagamento");
        cell.setCellStyle(style);
    }

    private void writeDataRows() {
        int rowCount = 1;
        for (OrderResponseDTO order : orders) {
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(order.getId());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(order.getUserEmail());
            sheet.autoSizeColumn(1);

            cell = row.createCell(2);
            cell.setCellValue(order.getMoment());
            sheet.autoSizeColumn(2);

            cell = row.createCell(3);
            cell.setCellValue(order.getStatus());
            sheet.autoSizeColumn(3);

            cell = row.createCell(4);
            cell.setCellValue(order.getTotalValue());
            sheet.autoSizeColumn(4);

            cell = row.createCell(5);
            cell.setCellValue(order.getDiscountPercentage());
            sheet.autoSizeColumn(5);

            cell = row.createCell(6);
            cell.setCellValue(order.getPaymentType());
            sheet.autoSizeColumn(6);
        }
    }

    public void export(HttpServletResponse response) {
        try {
            writeHeaderRow();
            writeDataRows();
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }
        catch (IOException e) {
            throw new BadRequestException("Erro ao gerar Excel");
        }
    }
}
