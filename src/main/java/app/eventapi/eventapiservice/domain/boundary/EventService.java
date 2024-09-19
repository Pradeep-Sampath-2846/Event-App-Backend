package app.eventapi.eventapiservice.domain.boundary;

import app.eventapi.eventapiservice.application.transport.BatchRequest;
import app.eventapi.eventapiservice.application.transport.EventCreateRequest;
import app.eventapi.eventapiservice.application.transport.EventUpdateRequest;
import app.eventapi.eventapiservice.domain.dto.EventDto;
import app.eventapi.eventapiservice.domain.exception.DomainException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Page<EventDto> getAllEvents(Pageable page) throws DomainException;

    Optional<EventDto> getEventById(String id) throws DomainException;

    EventDto createEvent(EventCreateRequest request) throws DomainException;

    EventDto updateEvent(String id, EventUpdateRequest request) throws DomainException;

    void deleteEvent(String id) throws DomainException;

    void processBatch(BatchRequest batchRequest) throws DomainException;
}
