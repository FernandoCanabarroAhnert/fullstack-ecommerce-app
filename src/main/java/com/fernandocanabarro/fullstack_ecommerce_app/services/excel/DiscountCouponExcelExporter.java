package com.fernandocanabarro.fullstack_ecommerce_app.services.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class DiscountCouponExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<DiscountCouponDTO> coupons;

    public DiscountCouponExcelExporter(List<DiscountCouponDTO> coupons) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Usuários");
        this.coupons = coupons;
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
        cell.setCellValue("Descrição");
        cell.setCellStyle(style);
        
        cell = row.createCell(2);
        cell.setCellValue("Código");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Desconto");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Dias Disponíveis");
        cell.setCellStyle(style);

    }

    private void writeDataRows() {
        int rowCount = 1;
        for (DiscountCouponDTO coupon : coupons) {
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(coupon.getId());
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(coupon.getDescription());
            sheet.autoSizeColumn(1);

            cell = row.createCell(2);
            cell.setCellValue(coupon.getCode());
            sheet.autoSizeColumn(2);

            cell = row.createCell(3);
            cell.setCellValue(coupon.getDiscountPercentage() + "%");
            sheet.autoSizeColumn(3);

            cell = row.createCell(4);
            cell.setCellValue(coupon.getAvailableDays());
            sheet.autoSizeColumn(4);
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
