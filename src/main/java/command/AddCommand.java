package command;

import parser.CommandParams;
import task.TaskList;
import ui.Ui;
import storage.Storage;

import exception.DukeException;

/**
 * Represents a specified command as AddCommand by extending the <code>Command</code> class.
 * Adds various specified type of tasks into the taskList. e.g event
 * Responses with the result.
 */
public class AddCommand extends Command {

    /**
     * Constructs an <code>AddCommand</code> object
     * with all components of the added task.
     *
     * @param commandParams parameters used to invoke the command.
     */
    public AddCommand(CommandParams commandParams) {
        super(commandParams);
    }

    /**
     * Lets the taskList of Duke add the task with the given information and
     * updates content of storage file according to new taskList.
     * Responses the result to user by using ui of Duke.
     *
     * @param tasks The taskList of Duke.
     * @param ui The ui of Duke.
     * @param storage The storage of Duke.
     * @throws DukeException If exceptions occur when adding tasks or updating storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        if (commandParams.getMainParam() == null) {
            throw new DukeException("☹ OOPS!!! The description of a task cannot be empty.");
        }
        switch (commandParams.getCommandType()) {
            case "todo":
                tasks.addToDo(commandParams.getMainParam());
                break;
            case "deadline":
                tasks.addDeadline(commandParams.getMainParam(), commandParams.getParam("by"));
                break;
            case "event":
                tasks.addEvent(commandParams.getMainParam(),
                        commandParams.getParam("start"),
                        commandParams.getParam("end"));
                break;
        }
        storage.update(tasks.toStorageStrings());

        ui.println("Got it. I've added this task:");
        ui.println(tasks.getTaskInfo(tasks.getSize() - 1));
        ui.println("Now you have " + tasks.getSize() + " tasks in the list.");
    }


}