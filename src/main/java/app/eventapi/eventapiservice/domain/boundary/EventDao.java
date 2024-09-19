package app.eventapi.eventapiservice.domain.boundary;

import app.eventapi.eventapiservice.domain.dto.EventDto;
import app.eventapi.eventapiservice.external.exception.DbException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventDao {
    Page<EventDto> findAllEvents(Pageable pageable) throws DbException;

    Optional<EventDto> findEventById(String eventId) throws DbException;

    EventDto createEvent(EventDto eventDto) throws DbException;

    EventDto updateEvent(EventDto eventDto) throws DbException;

    void deleteEvent(String id) throws DbException;

    void saveAll(List<EventDto> eventEntities);
}
