package com.controller;

import com.Csv.CSVFileService;
import com.model.Expense;
import com.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import com.Pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.model.Expense;
import com.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private PdfService pdfService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/ex")
    public String showExpenseManagementPage() {


        return "ex";
    }

    @GetMapping("/add")
    public String showAddExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "add-expense";
    }

    @PostMapping("/add")
    public String addExpense(@ModelAttribute("expense") Expense expense) {
        expenseService.saveExpense(expense);
        return "redirect:/expenses/list";
    }


    @GetMapping("/list")
    public String showAllExpenses(Model model) {
        List<Expense> expenses = expenseService.getAllExpenses();
        model.addAttribute("expenses", expenses);
        return "expense-list";
    }
    @GetMapping("/edit/{id}")
    public String showEditExpenseForm(@PathVariable("id") Long id, Model model) {
        Expense expense = expenseService.getExpenseById(id);
        model.addAttribute("expense", expense);
        return "edit-expense";
    }

    @PostMapping("/edit/{id}")
    public String editExpense(@PathVariable("id") Long id, @ModelAttribute("expense") Expense updatedExpense) {
        Expense editedExpense = expenseService.getExpenseById(id);
        if (editedExpense != null) {
            editedExpense.setExpenseName(updatedExpense.getExpenseName());
            editedExpense.setExpenseAmount(updatedExpense.getExpenseAmount());
            editedExpense.setExpenseDescription(updatedExpense.getExpenseDescription());
            expenseService.saveExpense(editedExpense);
        }
        return "redirect:/expenses/list";
    }
    // Endpoint to delete an expense
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        expenseService.deleteExpenseById(id);
        return "redirect:/expenses/list";
    }

    @GetMapping("/bulk/import")
    public String showBulkImportForm() {
        return "bulk-import";
    }

    @PostMapping("/bulk/import")
    public String bulkImportExpenses(@RequestParam("file") MultipartFile file) {
        try {
            expenseService.bulkImportExpenses(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/expenses/list";
    }

    // Endpoint to remove all expenses
    @GetMapping("/bulk/remove")
    public String removeAllExpenses() {
        expenseService.deleteAllExpenses();
        return "redirect:/expenses/list";
    }

    @GetMapping("/bulk/remove/{expenseName}")
    public String removeExpensesByName(@PathVariable("expenseName") String expenseName) {
        expenseService.deleteByExpenseName(expenseName);
        return "redirect:/expenses/list";
    }
    @GetMapping("/searchById")
    public String searchExpenseById(@RequestParam("id") Long id, Model model) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense != null) {
            model.addAttribute("expenses", List.of(expense));
        } else {
            model.addAttribute("expenses", List.of());
        }
        return "expense-list";
    }

    @GetMapping("/dashboard")
    public String showExpenseDashboard(Model model) {
        // Example data for expense trends
        List<BigDecimal> expenseAmounts = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            dates.add(currentDate.minusMonths(i).getMonth().name());
            expenseAmounts.add(expenseService.getTotalExpensesForMonth(currentDate.minusMonths(i)));
        }
        model.addAttribute("expenseTrendsDates", dates);
        model.addAttribute("expenseTrendsAmounts", expenseAmounts);

        // Example data for monthly spending summary
        List<MonthlySummary> monthlySummaries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDate month = currentDate.minusMonths(i);
            monthlySummaries.add(new MonthlySummary(month.getMonth().name(), expenseService.getTotalExpensesForMonth(month)));
        }
        model.addAttribute("monthlySummaries", monthlySummaries);

        return "dashboard";
    }

    public static class MonthlySummary {
        private String month;
        private BigDecimal totalExpenses;

        public MonthlySummary(String month, BigDecimal totalExpenses) {
            this.month = month;
            this.totalExpenses = totalExpenses;
        }

        public String getMonth() {
            return month;
        }

        public BigDecimal getTotalExpenses() {
            return totalExpenses;
        }
    }


    @GetMapping("/expenses/home")
    public String home(Model model) {
        double totalExpense = expenseService.calculateTotalExpense();
        model.addAttribute("totalExpense", totalExpense);
        return "home";
    }

    @GetMapping("/total")
    public String getTotalExpense(Model model) {
        double totalExpense = expenseService.calculateTotalExpense();
        model.addAttribute("totalExpense", totalExpense);
        return "total-expense";
    }






    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateExpenseReport() throws IOException {
        byte[] pdfBytes = pdfService.generateExpenseReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "expense_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
//    @Autowired
//    private CSVFileService csvFileService;
//
//    @GetMapping("/expenses/csv")
//    public String generateCSV() {
//        csvFileService.generateCSV();
//        return "redirect:/"; // Redirect to home or another appropriate page
//    }


    @Autowired
    private CSVFileService csvFileService;
    @GetMapping("/csv/generate")
    public String generateCSV() {
        csvFileService.generateCSV();
        return "CSV file generation initiated!";
    }

}
