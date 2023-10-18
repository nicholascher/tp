package seedu.address.storage.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.*;
import seedu.address.model.event.exceptions.TimeStartAfterTimeEndException;
import seedu.address.model.person.*;
import seedu.address.storage.JsonAdaptedPerson;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Jackson-friendly version of {@link Event}.
 */
public class JsonAdaptedEvent {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    private final String eventName;
    private final String timeStart;
    private final String timeEnd;
    private final String location;
    private final String eventDescription;
    private final List<JsonAdaptedPerson> clients = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("eventName") String eventName, @JsonProperty("timeStart") String timeStart,
                            @JsonProperty("timeEnd") String timeEnd, @JsonProperty("location") String location,
                            @JsonProperty("telegramName") String eventDescription, @JsonProperty("clients") List<JsonAdaptedPerson> clients) {
        this.eventName = eventName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.location = location;
        this.eventDescription = eventDescription;
        if (clients != null) {
            this.clients.addAll(clients);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedEvent(Event source) {
        eventName = source.getName().value;
        timeStart = source.getTimeStart().getValue();
        timeEnd = source.getTimeEnd().getValue();
        location = source.getLocation().value;
        eventDescription = source.getDescription().value;
        clients.addAll(source.getClients().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event.
     */
    public Event toModelType() throws IllegalValueException {
        final List<Person> eventClients = new ArrayList<>();
        for (JsonAdaptedPerson client : clients) {
            eventClients.add(client.toModelType());
        }

        if (eventName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, EventName.class.getSimpleName()));
        }
        if (!EventName.isValidEventName(eventName)) {
            throw new IllegalValueException(EventName.MESSAGE_CONSTRAINTS);
        }

        if (timeStart == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, TimeStart.class.getSimpleName()));
        }
        if (!TimeStart.isValidTime(timeStart)) {
            throw new IllegalValueException(TimeStart.MESSAGE_CONSTRAINTS);
        }

        if (location == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Location.class.getSimpleName()));
        }
        if (!Location.isValidLocation(location)) {
            throw new IllegalValueException(Location.MESSAGE_CONSTRAINTS);
        }

        if (eventDescription == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, EventDescription.class.getSimpleName()));
        }
        if (!EventDescription.isValidDescription(eventDescription)) {
            throw new IllegalValueException(EventDescription.MESSAGE_CONSTRAINTS);
        }

        final EventName modelEventName = new EventName(eventName);

        final TimeStart modelTimeStart = new TimeStart(timeStart);

        final TimeEnd modelTimeEnd = new TimeEnd(timeEnd);

        final Location modelLocation = new Location(location);

        final EventDescription modelEventDescription = new EventDescription(eventDescription);

        final Set<Person> modelClients = new HashSet<>(eventClients);

        try {
            return new Event(modelEventName, modelTimeStart, modelTimeEnd, modelClients, modelLocation, modelEventDescription);
        } catch (TimeStartAfterTimeEndException e) {
            throw new RuntimeException(e);
        }
    }
}
