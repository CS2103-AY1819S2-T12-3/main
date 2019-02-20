package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.medicine.Medicine;

/**
 * Provides a handle to a medicine card in the medicine list panel.
 */
public class MedicineCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String COMPANY_FIELD_ID = "#company";
    private static final String QUANTITY_FIELD_ID = "#quantity";
    private static final String EMAIL_FIELD_ID = "#email";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label companyLabel;
    private final Label quantityLabel;
    private final Label emailLabel;
    private final List<Label> tagLabels;

    public MedicineCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        nameLabel = getChildNode(NAME_FIELD_ID);
        companyLabel = getChildNode(COMPANY_FIELD_ID);
        quantityLabel = getChildNode(QUANTITY_FIELD_ID);
        emailLabel = getChildNode(EMAIL_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getCompany() {
        return companyLabel.getText();
    }

    public String getQuantity() {
        return quantityLabel.getText();
    }

    public String getEmail() {
        return emailLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code medicine}.
     */
    public boolean equals(Medicine medicine) {
        return getName().equals(medicine.getName().fullName)
                && getCompany().equals(medicine.getCompany().value)
                && getQuantity().equals(medicine.getQuantity().value)
                && getEmail().equals(medicine.getEmail().value)
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(medicine.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
