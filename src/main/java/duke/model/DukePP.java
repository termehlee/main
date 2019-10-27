package duke.model;

import duke.commons.LogsCenter;
import duke.exception.DukeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Wraps all memory data of Duke++
 * Implements the interface of model module.
 */

public class DukePP implements Model {

    private static final Logger logger = LogsCenter.getLogger(DukePP.class);

    private final ExpenseList expenseList;
    private final PlanBot planBot;
    private final IncomeList incomeList;
    private final Budget budget;
    // todo: add other data inside the DukePP.

    public ObservableList<Expense> externalExpenseList;
    public ObservableList<Income> externalIncomeList;


    /**
     * Creates a DukePP.
     * This constructor is used for loading DukePP from storage.
     */
    // todo: pass more arguments to constructor as more data are implemented.
    public DukePP(ExpenseList expenseList, Map<String, String> planAttributes, IncomeList incomeList, Budget budget) throws DukeException {
        this.expenseList = expenseList;
        this.planBot = new PlanBot(planAttributes);
        this.incomeList = incomeList;
        this.budget = budget;
    }

    //******************************** ExpenseList operations

    public void addExpense(Expense expense) {
        expenseList.add(expense);
        logger.info("Model's expense externalList length now is "
                + externalExpenseList.size());
    }

    public void deleteExpense(int index) throws DukeException {
        expenseList.remove(index);
    }

    public void clearExpense() {
        expenseList.clear();
    }

    public void filterExpense(String filterCriteria) throws DukeException {
        expenseList.setFilterCriteria(filterCriteria);
    }

    public void sortExpense(String sortCriteria) throws DukeException {
        expenseList.setSortCriteria(sortCriteria);
    }

    public void viewExpense(String viewScope, int previous) throws DukeException {
        expenseList.setViewScope(viewScope, previous);
    }

    public ObservableList<Expense> getExpenseExternalList() {
        logger.info("Model sends external expense list length "
                + expenseList.getExternalList().size());
        externalExpenseList = FXCollections.unmodifiableObservableList(expenseList.getExternalList());
        return externalExpenseList;
    }

    /**
     * Returns the expenseList for storage.
     */
    public ExpenseList getExpenseList() {
        return expenseList;
    }

    @Override
    public String getMonthlyBudgetString() {
        return budget.getMonthlyBudgetString();
    }

    @Override
    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        budget.setMonthlyBudget(monthlyBudget);
    }

    @Override
    public void setCategoryBudget(String category, BigDecimal budgetBD) {
        budget.setCategoryBudget(category, budgetBD);
    }

    @Override
    public BigDecimal getRemaining(BigDecimal total) {
        return budget.getRemaining(total);
    }

    @Override
    public Map<String, BigDecimal> getBudgetCategory() {
        return budget.getBudgetCategory();
    }

    @Override
    public Budget getBudget() {
        return budget;
    }

    @Override
    public ObservableList<String> getBudgetObservableList() {
        return budget.getBudgetObservableList();
    }


    //************************************************************
    // PlanBot operations

    public ObservableList<PlanBot.PlanDialog> getDialogObservableList() {
        return planBot.getDialogObservableList();
    }

    public void processPlanInput(String input) throws DukeException {
        planBot.processInput(input);
    }

    @Override
    public Map<String, String> getKnownPlanAttributes() {
        return planBot.getPlanAttributes();
    }
  
    @Override
    public PlanQuestionBank.PlanRecommendation getRecommendedBudgetPlan() {
        return planBot.getPlanBudgetRecommendation();
    }

    //************************************************************ IncomeList operations
    public void addIncome(Income income) {
        incomeList.add(income);
        logger.info("Model's income externalList length now is "
                + externalIncomeList.size());
    }

    public void deleteIncome(int index) throws DukeException {
        incomeList.remove(index);
    }

    public void clearIncome() {
        incomeList.clear();
    }

    public void filterIncome(String filterCriteria) throws DukeException {
        expenseList.setFilterCriteria(filterCriteria);
    }

    public void sortIncome(String sortCriteria) throws DukeException {
        expenseList.setSortCriteria(sortCriteria);
    }

    public void viewIncome(String viewScope, int previous) throws DukeException {
        expenseList.setViewScope(viewScope, previous);
    }

    public ObservableList<Income> getIncomeExternalList() {
        logger.info("Model sends external income list length "
                + incomeList.getExternalList().size());
        externalIncomeList = FXCollections.unmodifiableObservableList(incomeList.getExternalList());
        return externalIncomeList;
    }

    public IncomeList getIncomeList() {
        return incomeList;
    }

    //******************************** Operations for other data....
    //******************************** For example, operations of monthly income list.
    //    todo: add other data operations

}