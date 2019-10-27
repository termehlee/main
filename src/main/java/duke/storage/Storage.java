package duke.storage;

import duke.exception.DukeException;
import duke.model.Budget;
import duke.model.ExpenseList;
import duke.model.IncomeList;

import java.io.IOException;
import java.util.Map;

/**
 * API of the Storage component
 */
public interface Storage {

    void saveExpenseList(ExpenseList expenseList) throws DukeException;

    ExpenseList loadExpenseList() throws DukeException;

    void savePlanAttributes(Map<String, String> attributes) throws DukeException;

    Map<String, String> loadPlanAttributes();

    void saveIncomeList(IncomeList incomeList) throws DukeException;

    IncomeList loadIncomeList() throws DukeException;

    Budget loadBudget() throws IOException, DukeException;

    void saveBudget(Budget budget) throws DukeException;

    // todo: add other interface methods for other lists.
}