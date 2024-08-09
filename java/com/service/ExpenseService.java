package com.service;

import com.dao.ExpenseRepository;
import com.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Method to save or update an expense
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Method to retrieve all expenses
//    public List<Expense> getAllExpenses() {
//        return expenseRepository.findAll();
//    }

    // Method to retrieve an expense by ID
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    // Method to delete an expense by ID
    public void deleteExpenseById(Long id) {
        expenseRepository.deleteById(id);
    }

    // Method to find expenses by name
    public List<Expense> findExpensesByName(String expenseName) {
        return expenseRepository.findByExpenseName(expenseName);
    }

    // Method for bulk import of expenses from a CSV file
    public void bulkImportExpenses(MultipartFile file) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Expense expense = new Expense();
                expense.setExpenseName(data[0]);
                expense.setExpenseAmount(Double.parseDouble(data[1]));
                expense.setExpenseDescription(data[2]);
                expenseRepository.save(expense);
            }
        }
    }

    // Method to delete all expenses
    public void deleteAllExpenses() {
        expenseRepository.deleteAll();
    }

    // Method to delete expenses by name
    public void deleteByExpenseName(String expenseName) {
        expenseRepository.deleteByExpenseName(expenseName);
    }

    // Method to edit an expense
    public Expense editExpense(Long id, Expense updatedExpense) {
        Expense existingExpense = expenseRepository.findById(id).orElse(null);
        if (existingExpense != null) {
            existingExpense.setExpenseName(updatedExpense.getExpenseName());
            existingExpense.setExpenseAmount(updatedExpense.getExpenseAmount());
            existingExpense.setExpenseDescription(updatedExpense.getExpenseDescription());
            return expenseRepository.save(existingExpense);
        }
        return null; // Handle scenario where expense with given ID is not found
    }
    public List<Expense> searchExpenses(String keyword) {
        return expenseRepository.findByExpenseNameContainingIgnoreCaseOrExpenseDescriptionContainingIgnoreCase(keyword, keyword);
    }public BigDecimal getTotalExpensesForMonth(LocalDate month) {
        // Implement your logic to get total expenses for the given month
        return new BigDecimal("100.00"); // Example value
    }
    // Method to retrieve all expenses (for listing or reporting)
    public List<Expense> getAllExpenses() {
        // Fetch all expenses from the database using JpaRepository method
        return expenseRepository.findAll();
    }
    // Other methods as per your application's requirements


    public double calculateTotalExpense() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream().mapToDouble(Expense::getExpenseAmount).sum();
    }
}
