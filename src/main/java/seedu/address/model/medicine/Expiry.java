package seedu.address.model.medicine;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents the expiry date of a batch Medicine in the inventory.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Expiry implements Comparable<Expiry> {
    public static final int MAX_DAYS_TO_EXPIRY = 1000000000; // one trillion days, equivalent to 2,739,726 years
    public static final String MESSAGE_CONSTRAINTS =
            "Expiry date should be of the format dd/mm/yyyy and should be a valid date.\n" +
            "Expiry date should not be more than " + Integer.toString(MAX_DAYS_TO_EXPIRY) + " days from today.\n";

    private final LocalDate expiryDate;

    /**
     * Constructs an {@code Expiry}.
     *
     * @param expiry A valid expiry date.
     */
    public Expiry(String expiry) {
        requireNonNull(expiry);

        checkArgument(isValidDate(expiry), MESSAGE_CONSTRAINTS);
        if (expiry.equals("-")) {
            this.expiryDate = null;
        } else {
            this.expiryDate = parseRawDate(expiry);
        }
    }

    /**
     * Returns if a given string is a valid expiry.
     * */
    public static boolean isValidDate(String test) {
        if (test.equals("-")) {
            return true;
        }

        try {
            LocalDate parsed = parseRawDate(test);
            if (parsed.isAfter(LocalDate.now().plusDays(MAX_DAYS_TO_EXPIRY))) {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private static LocalDate parseRawDate(String expiry) {
        return LocalDate.parse(expiry, DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT));
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public boolean isExpired() {
        return this.expiryDate.compareTo(LocalDate.now()) < 0;
    }

    @Override
    public int compareTo(Expiry o) {
        LocalDate date1 = this.expiryDate;
        LocalDate date2 = o.getExpiryDate();
        if (date1 == null) {
            return -1;
        } else if (date2 == null) {
            return 1;
        } else {
            return date1.compareTo(date2);
        }
    }

    @Override
    public String toString() {
        if (this.expiryDate == null) {
            return "-";
        } else {
            return expiryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            // short circuit if same object
            return true;
        }

        if (other instanceof Expiry) {
            if (expiryDate == null) {
                return ((Expiry) other).getExpiryDate() == null;
            } else {
                return expiryDate.equals(((Expiry) other).getExpiryDate());
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return expiryDate.hashCode();
    }

}
