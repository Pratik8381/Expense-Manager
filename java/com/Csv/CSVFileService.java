package com.Csv;

import com.dao.ExpenseRepository;
import com.model.Expense;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVFileService {

    @Autowired
    private ExpenseRepository expenseRepository;

//    public void generateCSV() {
//        List<Expense> expenses = expenseRepository.findAll();
//
//        String csvFilePath = "expenses.csv";
//
//        try (FileWriter writer = new FileWriter(csvFilePath)) {
//            // Write CSV header
//            writer.append("id,expenseName,expenseAmount,expenseDescription\n");
//
//            // Write each expense to CSV
//            for (Expense expense : expenses) {
//                writer.append(String.join(",",
//                        String.valueOf(expense.getId()),
//                        expense.getExpenseName(),
//                        String.valueOf(expense.getExpenseAmount()),
//                        expense.getExpenseDescription()
//                ));
//                writer.append("\n");
//            }
//
//            System.out.println("CSV file was created successfully!");
//
//        } catch (IOException e) {
//            System.out.println("Error writing CSV file: " + e.getMessage());
//        }
//    }

    public String generateCSV() {
        List<Expense> expenses = expenseRepository.findAll();

        String csvFilePath = "expenses.csv";

        try (FileWriter writer = new FileWriter(csvFilePath)) {
            // Write CSV header
            writer.append("id,expenseName,expenseAmount,expenseDescription\n");

            // Write each expense to CSV
            for (Expense expense : expenses) {
                writer.append(String.join(",",
                        String.valueOf(expense.getId()),
                        expense.getExpenseName(),
                        String.valueOf(expense.getExpenseAmount()),
                        expense.getExpenseDescription()
                ));
                writer.append("\n");
            }

            System.out.println("CSV file was created successfully!");
            return csvFilePath; // Return the path to the generated CSV file

        } catch (IOException e) {
            System.out.println("Error writing CSV file: " + e.getMessage());
            return null; // Handle error appropriately, possibly throw an exception
        }
    }

    public int importCSV(MultipartFile file) throws CsvValidationException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = reader.readNext(); // Assuming first row is header
            if (header == null || header.length != 4) {
                throw new IOException("Invalid CSV file format");
            }

            List<Expense> expenses = new ArrayList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                Expense expense = new Expense();
                expense.setExpenseName(line[1]);
                expense.setExpenseAmount(Double.parseDouble(line[2]));
                expense.setExpenseDescription(line[3]);
                // You might need to handle ID generation or leave it to auto-increment
                expenses.add(expense);
            }

            // Save all expenses in one go for efficiency
            List<Expense> savedExpenses = expenseRepository.saveAll(expenses);
            return savedExpenses.size();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle exceptions appropriately
            return 0;
        }
    }
}
