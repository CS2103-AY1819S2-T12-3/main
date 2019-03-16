package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_MEDICINES_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalMedicines.ACETAMINOPHEN;
import static seedu.address.testutil.TypicalMedicines.IBUPROFEN;
import static seedu.address.testutil.TypicalMedicines.KEYWORD_MATCHING_SODIUM;
import static seedu.address.testutil.TypicalMedicines.LEVOTHYROXINE;
import static seedu.address.testutil.TypicalMedicines.NAPROXEN;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

public class FindCommandSystemTest extends MediTabsSystemTest {

    @Test
    public void find() {
        /* Case: find multiple medicines in inventory, command with leading spaces and trailing spaces
         * -> 2 medicines found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_SODIUM + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, NAPROXEN, LEVOTHYROXINE); // Naproxen Sodium and Levothyroxine Sodium
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where medicine list is displaying the medicines we are finding
         * -> 2 medicines found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_SODIUM;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find medicine where medicine list is not displaying the medicine we are finding -> 1 medicine found */
        command = FindCommand.COMMAND_WORD + " Acetaminophen";
        ModelHelper.setFilteredList(expectedModel, ACETAMINOPHEN);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple medicines in inventory, 2 keywords -> 2 medicines found */
        command = FindCommand.COMMAND_WORD + " Ibuprofen Levothyroxine";
        ModelHelper.setFilteredList(expectedModel, IBUPROFEN, LEVOTHYROXINE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple medicines in inventory, 2 keywords in reversed order -> 2 medicines found */
        command = FindCommand.COMMAND_WORD + " Levothyroxine Ibuprofen";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple medicines in inventory, 2 keywords with 1 repeat -> 2 medicines found */
        command = FindCommand.COMMAND_WORD + " Levothyroxine Ibuprofen Levothyroxine";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple medicines in inventory, 2 matching keywords and 1 non-matching keyword
         * -> 2 medicines found
         */
        command = FindCommand.COMMAND_WORD + " Levothyroxine Ibuprofen NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same medicines in inventory after deleting 1 of them -> 1 medicine found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getInventory().getMedicineList().contains(IBUPROFEN));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_SODIUM;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, LEVOTHYROXINE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find medicine in inventory, keyword is same as name but of different case -> 1 medicine found */
        command = FindCommand.COMMAND_WORD + " SoDiUm";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find medicine in inventory, keyword is substring of name -> 0 medicines found */
        command = FindCommand.COMMAND_WORD + " Sodi";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find medicine in inventory, name is substring of keyword -> 0 medicines found */
        command = FindCommand.COMMAND_WORD + " Sodiums";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find medicine not in inventory -> 0 medicines found */
        command = FindCommand.COMMAND_WORD + " Augmentin";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find quantity of medicine in inventory -> 0 medicines found */
        command = FindCommand.COMMAND_WORD + " " + LEVOTHYROXINE.getQuantity().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find company of medicine in inventory -> 0 medicines found */
        command = FindCommand.COMMAND_WORD + " " + LEVOTHYROXINE.getCompany().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expiry date of medicine in inventory -> 0 medicines found */
        command = FindCommand.COMMAND_WORD + " " + LEVOTHYROXINE.getExpiry().toString();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of medicine in inventory -> 0 medicines found */
        List<Tag> tags = new ArrayList<>(LEVOTHYROXINE.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a medicine is selected -> selected card deselected */
        showAllMedicines();
        selectMedicine(Index.fromOneBased(1));
        assertFalse(getMedicineListPanel().getHandleToSelectedCard().getName()
                .equals(LEVOTHYROXINE.getName().fullName));
        command = FindCommand.COMMAND_WORD + " Levothyroxine";
        ModelHelper.setFilteredList(expectedModel, LEVOTHYROXINE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find medicine in empty inventory -> 0 medicines found */
        deleteAllMedicines();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_SODIUM;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, LEVOTHYROXINE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Sodium";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_MEDICINES_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code MediTabsSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see MediTabsSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_MEDICINES_LISTED_OVERVIEW, expectedModel.getFilteredMedicineList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code MediTabsSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the loaded information table, selected card and status bar remain unchanged, and the command
     * box has the error style.
     * @see MediTabsSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
