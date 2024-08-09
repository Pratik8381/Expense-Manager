package com.dao;
import com.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Custom query methods (if needed)
     List<Expense> findByExpenseName(String expenseName);


    List<Expense> findByExpenseNameContainingIgnoreCaseOrExpenseDescriptionContainingIgnoreCase(String nameKeyword, String descriptionKeyword);

    void deleteByExpenseName(String expenseName);
    List<Expense> findByExpenseNameContainingIgnoreCase(String keyword);

    // Example: Find expenses by description containing the provided keyword (case-insensitive)
    List<Expense> findByExpenseDescriptionContainingIgnoreCase(String keyword);

}
