package seedu.address.logic.parser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalPersons.getTypicalPersonsBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.events.AddEventCommand;
import seedu.address.logic.commands.events.DeleteEventCommand;
import seedu.address.logic.commands.events.ListEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.EventsBook;
import seedu.address.model.FinancesBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.EventUtil;
import seedu.address.testutil.PersonBuilder;


public class EventParserTest {

    private final EventParser parser = new EventParser();
    private final ModelManager model = new ModelManager(getTypicalPersonsBook(), new EventsBook(), new FinancesBook(), new UserPrefs());

    @Test
    public void parseCommand_add() throws Exception {
        Event event = new EventBuilder().build();
        model.addPerson(new PersonBuilder().withName("Alice").build());
        AddEventCommand command = (AddEventCommand) parser.parseCommand(EventUtil.getAddCommand(event));

        assertEquals(new AddEventCommand(event).execute(model), command.execute(model));
    }

    /* TODO IMPLEMENT CLEAR TEST WHEN IMPLEMENTED*/

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteEventCommand command = (DeleteEventCommand) parser.parseCommand(
                DeleteEventCommand.COMMAND_WORD + " " + INDEX_FIRST_EVENT.getOneBased());
        assertEquals(new DeleteEventCommand(INDEX_FIRST_EVENT), command);
    }

    /* TODO IMPLEMENT EDIT TEST WHEN IMPLEMENTED */

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    /* TODO IMPLEMENT FIND TEST WHEN IMPLEMENTED */

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListEventCommand.COMMAND_WORD) instanceof ListEventCommand);
        assertTrue(parser.parseCommand(ListEventCommand.COMMAND_WORD + " 3") instanceof ListEventCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
