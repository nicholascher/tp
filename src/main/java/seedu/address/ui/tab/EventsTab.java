package seedu.address.ui.tab;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.CommandBox;
import seedu.address.ui.EventListPanel;
import seedu.address.ui.HelpWindow;
import seedu.address.ui.ResultDisplay;
import seedu.address.ui.UiPart;

/**
 * The Events Tab.
 */
public class EventsTab extends UiPart<Region> {
    private static final String FXML = "EventsTab.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private EventListPanel eventListPanel;
    private ResultDisplay resultDisplay;

    private final HelpWindow helpWindow;

    @FXML
    private TabPane tabPane;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane eventListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    /**
     * Creates a {@code EventsTab}.
     */
    public EventsTab(TabPane tabPane) {
        super(FXML);
        helpWindow = new HelpWindow();
        this.tabPane = tabPane;
    }

    public void setup(Logic logic) {
        this.logic = logic;
        setInnerParts();
    }

    private void setInnerParts() {
        eventListPanel = new EventListPanel(logic.getEventList());
        eventListPanelPlaceholder.getChildren().add(eventListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    public EventListPanel getEventListPanel() {
        return eventListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isChangeTab()) {
                handleTabChange(commandResult.getTabToChange());
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    // TODO: Implement better exit handling within tabs.
    private void handleExit() {
        System.exit(0);
    }

    private void handleTabChange(int tabIndex) {
        tabPane.getSelectionModel().select(tabIndex);
    }
}
