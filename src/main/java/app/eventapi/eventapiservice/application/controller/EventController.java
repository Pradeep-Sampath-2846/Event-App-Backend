package app.eventapi.eventapiservice.application.controller;

import app.eventapi.eventapiservice.application.transport.BatchRequest;
import app.eventapi.eventapiservice.application.transport.EventCreateRequest;
import app.eventapi.eventapiservice.application.transport.EventUpdateRequest;
import app.eventapi.eventapiservice.domain.boundary.EventService;
import app.eventapi.eventapiservice.domain.dto.EventDto;
import app.eventapi.eventapiservice.domain.exception.DomainException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${base-url}/events")
@CrossOrigin("*")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Page<EventDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventDto> eventList = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable String id) {
        try{
            Optional<EventDto> event = eventService.getEventById(id);
            return event.map(ResponseEntity::ok)
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }catch (DomainException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventCreateRequest request) {
        EventDto newEvent = eventService.createEvent(request);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable String id, @RequestBody EventUpdateRequest request) {
        try {
            EventDto updatedEvent = eventService.updateEvent(id, request);
            return ResponseEntity.ok(updatedEvent);
        } catch (DomainException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        try {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DomainException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> createBatchEvents(@RequestBody BatchRequest batchRequest) {
        eventService.processBatch(batchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/batch/file")
    public ResponseEntity<Void> createBatchEvents(@RequestParam("file") MultipartFile file) {
        try {
            String jsonData = new String(file.getBytes());

            ObjectMapper objectMapper = new ObjectMapper();
            BatchRequest batchRequest = objectMapper.readValue(jsonData, BatchRequest.class);

            eventService.processBatch(batchRequest);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
