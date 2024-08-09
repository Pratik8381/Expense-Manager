package com.Pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.model.Expense;
import com.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private ExpenseService expenseService;

    public byte[] generateExpenseReport() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add title
        Paragraph title = new Paragraph("Expense Report")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(20)
                .setFontColor(ColorConstants.BLACK);
        document.add(title);

        // Add table
        float[] columnWidths = {1, 5, 2, 4};
        Table table = new Table(columnWidths);

        // Add table headers
        table.addHeaderCell("ID");
        table.addHeaderCell("Name");
        table.addHeaderCell("Amount");
        table.addHeaderCell("Description");

        // Fetch expenses from service
        List<Expense> expenses = expenseService.getAllExpenses();
        for (Expense expense : expenses) {
            table.addCell(String.valueOf(expense.getId()));
            table.addCell(expense.getExpenseName());
            table.addCell(String.valueOf(expense.getExpenseAmount()));
            table.addCell(expense.getExpenseDescription());
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
