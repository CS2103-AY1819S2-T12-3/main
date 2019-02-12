package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.util.FileName;

/**
 * Exports the current list to a csv file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": exports the current list to a .csv file. "
            + "Parameters: "
            + "FILENAME\n"
            + "Example: " + COMMAND_WORD + " "
            + "example";

    public static final String MESSAGE_SUCCESS = "The current list has been exported to: %1$s.csv";

    private final String fileName;

    /**
     * Creates an ExportCommand to export the current list to a .csv file.
     */
    public ExportCommand(FileName fileName) {
        requireNonNull(fileName);
        this.fileName = fileName.toString();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        // TODO: 12/2/2019 Export to CSV implementation
        return new CommandResult(String.format(MESSAGE_SUCCESS, fileName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExportCommand // instanceof handles nulls
                && fileName.equals(((ExportCommand) other).fileName));
    }
}
