package duke.command;

import duke.dukeobject.ExpenseList;
import duke.parser.CommandParams;
import duke.ui.Ui;

/**
 * Represents a specified command as ListCommand by extending the {@code Command} class.
 * Lists all expensesList in ExpenseList of Duke.
 * Responses with the result.
 */
public class ListCommand extends Command {
    /**
     * Constructs a {@code ListCommand} object.
     */
    public ListCommand() {
        super(null, null, null, null);
    }

    /**
     * Lists all expensesList in ExpenseList of Duke by using ui of Duke.
     *  @param expensesList The ExpenseList of Duke.
     * @param ui           The ui of Duke.
     */
    public void execute(CommandParams commandParams, ExpenseList expensesList, Ui ui) {
        if (expensesList.internalSize() == 0) {
            ui.println("Oops, you haven't added any task!");
        } else {
            ui.println("Here are the expensesList in your list:");
            for (int i = 1; i <= expensesList.internalSize(); i++) {
                ui.println(i + ". " + expensesList.get(i).toString());
            }
        }
    }
}