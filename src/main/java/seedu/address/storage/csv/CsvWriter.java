package seedu.address.storage.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.CSVWriter;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;

/**
 * A CsvWriter class to complement the export command for MediTabs.
 * It uses Opencsv library from http://opencsv.sourceforge.net/index.html
 *
 */
public class CsvWriter {

    private static final String FILE_OPS_ERROR_MESSAGE = "Could not export data to csv file: ";
    private static String[] defaultHeading = {"Name", "Quantity", "Expiry Date", "Company", "Tags"};
    private static final Path DEFAULT_EXPORT_FOLDER_PATH = Paths.get("exported");
    private String csvFileName;
    private Model model;
    private Path csvFilePath;

    public CsvWriter(String csvFileName, Model model) {
        this.csvFileName = csvFileName;
        this.model = model;
    }

    /**
     * Exports the current list in the GUI to a csv file.
     * @throws CommandException If there is an error exporting the current list in the GUI to a csv file.
     */
    public void export() throws CommandException {
        List<Medicine> currentGuiList = model.getFilteredMedicineList();
        createCsvFile(csvFileName);
        writeDataToCsv(currentGuiList);
    }

    /**
     * Creates a .csv file based on the input csv file name.
     * @param csvFileName The input csv file name.
     * @throws CommandException If there are errors creating the .csv file.
     */
    private void createCsvFile(String csvFileName) throws CommandException {
        createIfExportDirectoryMissing();
        try {
            csvFilePath = Files.createFile(Paths.get("exported", csvFileName + ".csv"));
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }
    }

    /**
     * Writes data from current list displayed in GUI when export command is called to a csv file.
     * @param currentGuiList The current list displayed in the GUI when the export command is called.
     * @throws CommandException If tere is an error exporting the current list in the GUI to a csv file.
     */
    private void writeDataToCsv(List currentGuiList) throws CommandException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath.toString()))) {
            csvWriter.writeNext(defaultHeading);
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }
    }

    /**
     * Creates the export directory if export directory where exported data are stored.
     * @throws CommandException if there are errors creating the export directory.
     */
    private void createIfExportDirectoryMissing() throws CommandException {
        if (Files.isDirectory(DEFAULT_EXPORT_FOLDER_PATH) == false) {
            try {
                Files.createDirectory(Paths.get("exported"));
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }
    }

}
