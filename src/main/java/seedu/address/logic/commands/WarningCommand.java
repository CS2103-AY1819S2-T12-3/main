package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;

import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.warning.WarningPanelPredicateAccessor;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.medicine.MedicineExpiryThresholdPredicate;
import seedu.address.model.medicine.MedicineLowStockThresholdPredicate;
import seedu.address.model.threshold.Threshold;


/**
 * Change or show thresholds used in warning panel.
 */
public class WarningCommand extends Command {

    public static final String COMMAND_WORD = "warning";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Change or check thresholds used in the warning panel. "
            + "Parameters: "
            + "[" + PREFIX_EXPIRY + "EXPIRY_THRESHOLD" + "] "
            + "[" + PREFIX_QUANTITY + "LOW_STOCK_THRESHOLD] "
            + "[" + "SHOW" + "]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_QUANTITY + "20\n"
            + "NOTE: only one parameter allowed.";

    private final Predicate predicate;
    private final boolean showThreshold;

    /**
     * Creates a WarningCommand to change the specified warning threshold.
     */
    public WarningCommand(Predicate predicate) {
        requireNonNull(predicate);
        this.showThreshold = false;
        this.predicate = predicate;
    }

    /**
     * Creates a WarningCommand to show the current threshold levels.
     */
    public WarningCommand(String show) {
        requireNonNull(show);
        this.showThreshold = true;
        this.predicate = null;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        WarningPanelPredicateAccessor predicateAccessor = model.getWarningPanelPredicateAccessor();

        if (showThreshold) {
            return showCurrentThresholds(predicateAccessor);
        } else {
            return changeThreshold(model, predicateAccessor);
        }
    }

    /**
     * Show current thresholds used in warning panel.
     * @param predicateAccessor
     * @return Message with current thresholds used in warning panel
     */
    private CommandResult showCurrentThresholds(WarningPanelPredicateAccessor predicateAccessor) {
        return new CommandResult(
                String.format(Messages.MESSAGE_SHOW_CURRENT_THRESHOLDS,
                        predicateAccessor.getExpiryThreshold().getNumericValue(),
                        predicateAccessor.getLowStockThreshold().getNumericValue()));
    }

    /**
     * Change threshold used in warning panel.
     * @param model
     * @param predicateAccessor
     * @return Message with current thresholds used in warning panel
     */
    private CommandResult changeThreshold(Model model, WarningPanelPredicateAccessor predicateAccessor) {
        if (predicate instanceof MedicineExpiryThresholdPredicate) {
            Threshold threshold = ((MedicineExpiryThresholdPredicate) predicate).getThreshold();
            model.updateFilteredExpiringMedicineList(new MedicineExpiryThresholdPredicate(threshold));
            predicateAccessor.setMedicineExpiringThreshold(threshold.getNumericValue());
            predicateAccessor.setBatchExpiringThreshold(threshold.getNumericValue());

        } else if (predicate instanceof MedicineLowStockThresholdPredicate) {
            Threshold threshold = ((MedicineLowStockThresholdPredicate) predicate).getThreshold();
            model.updateFilteredLowStockMedicineList(new MedicineLowStockThresholdPredicate(threshold));
            predicateAccessor.setMedicineLowStockThreshold(threshold.getNumericValue());
        }

        return showCurrentThresholds(predicateAccessor);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WarningCommand // instanceof handles nulls
                && predicate.equals(((WarningCommand) other).predicate)
                && showThreshold == ((WarningCommand) other).showThreshold);
    }
}
