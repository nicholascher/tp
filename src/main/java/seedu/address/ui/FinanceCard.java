package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.finance.Finance;

/**
 * A UI component that displays information of a {@code Finance}.
 */
public class FinanceCard extends UiPart<Region> {
    private static final String FXML = "FinanceListCard.fxml";
    public final Finance finance;
    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label amount;
    @FXML
    private Label client;
    @FXML
    private Label description;
    /**
     * Creates a {@code PersonCode} with the given {@code Finance} and index to display.
     */
    public FinanceCard(Finance finance, int displayedIndex) {
        super(FXML);
        this.finance = finance;
        id.setText(displayedIndex + ". ");
        amount.setText(finance.getAmount().value);
        if (finance.getClient() != null) {
            client.setText(finance.getClient().fullName);
        } else {
            client.setText("No client");
        }
        description.setText(finance.getDescription().value);
    }
}