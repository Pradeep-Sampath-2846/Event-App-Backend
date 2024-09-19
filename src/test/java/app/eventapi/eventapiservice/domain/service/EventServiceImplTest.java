package app.eventapi.eventapiservice.domain.service;

import app.eventapi.eventapiservice.application.transport.*;
import app.eventapi.eventapiservice.domain.boundary.EventDao;
import app.eventapi.eventapiservice.domain.dto.EventDto;
import app.eventapi.eventapiservice.domain.exception.DomainException;
import app.eventapi.eventapiservice.external.exception.DbException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventDao eventDao;
    @Mock
    private TransactionService transactionService;
    @Spy
    private ModelMapper modelMapper;
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventDao,transactionService,modelMapper,5);
    }

    @Test
    @DisplayName("Get All Event success Test")
    void getAllEvents() {
        List<EventDto> eventDtoList = Arrays.asList(
                new EventDto("event_id_01", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", ""),
                new EventDto("event_id_02", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", ""),
                new EventDto("event_id_03", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", "")
                );
        PageImpl<EventDto> page = new PageImpl<>(eventDtoList, PageRequest.of(1, 10), 10);
        when(eventDao.findAllEvents(any())).thenReturn(page);

        assertDoesNotThrow(()->eventService.getAllEvents(PageRequest.of(1, 10)));
    }
    @Test
    @DisplayName("Get All Event fail Test")
    void getAllEvents_01() {

        when(eventDao.findAllEvents(any())).thenThrow(DbException.class);

        assertThrows(DomainException.class,()->eventService.getAllEvents(PageRequest.of(1, 10)));
    }

    @Test
    @DisplayName("Get Event by Id Success Test")
    void getEventById() {
        EventDto eventDto = new EventDto("event_id_01", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", "");
        when(eventDao.findEventById(any())).thenReturn(Optional.of(eventDto));

        Optional<EventDto> eventOpt = eventService.getEventById("event_id_01");

        assertTrue(eventOpt.isPresent());
        assertEquals(eventDto.getEventId(),eventOpt.get().getEventId());
    }
    @Test
    @DisplayName("Get Event by Id fail Test")
    void getEventById_01() {
        when(eventDao.findEventById(any())).thenThrow(DbException.class);
        assertThrows(DomainException.class,()->eventService.getEventById("event_id_01"));
    }

    @Test
    @DisplayName("Create Event success Test")
    void createEvent() {
        EventCreateRequest request = new EventCreateRequest("event_id_01", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", "");
        when(eventDao.createEvent(any())).thenReturn(EventDto.builder().eventId("event_id_01").build());

        EventDto event = eventService.createEvent(request);

        assertEquals(request.getEventId(),event.getEventId());
    }
    @Test
    @DisplayName("Create Event fail test")
    void createEvent2() {
        EventCreateRequest request = new EventCreateRequest("event_id_01", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", "");
        when(eventDao.createEvent(any())).thenThrow(DbException.class);

        assertThrows(DomainException.class,()->eventService.createEvent(request));
    }

    @Test
    @DisplayName("Update Event success test")
    void updateEvent() {
        EventUpdateRequest request = new EventUpdateRequest("event_id_01", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", "");
        when(eventDao.updateEvent(any())).thenReturn(EventDto.builder().eventId("event_id_01").build());

        EventDto event = eventService.updateEvent("event_id_01",request);

        assertEquals(request.getEventId(),event.getEventId());
    }
    @Test
    @DisplayName("Update event fail test")
    void updateEvent2() {
        EventUpdateRequest request = new EventUpdateRequest("event_id_01", "tr001", "20151022102011927EDT", "10001", "RPS-00001", 1, "DESTINATION", "", "", "");
        when(eventDao.updateEvent(any())).thenThrow(DbException.class);

        assertThrows(DomainException.class,()->eventService.updateEvent("event_id_01",request));
    }

    @Test
    @DisplayName("Delete event success test")
    void deleteEvent() {
        doNothing().when(eventDao).deleteEvent(any());
        assertDoesNotThrow(()->eventService.deleteEvent("event_id_01"));
    }
    @Test
    @DisplayName("Delete event fail test")
    void deleteEvent2() {
        doThrow(DbException.class).when(eventDao).deleteEvent(any());
        assertThrows(DomainException.class,()->eventService.deleteEvent("event_id_01"));
    }

    @Test
    @DisplayName("Process batch request success test")
    void processBatch() {
        List<TransactionRecord> records = Arrays.asList(
                new TransactionRecord(
                        "trId01",
                        "20151022102011927EDT",
                        "rc01",
                        "cli-01",
                        Arrays.asList(
                                new EventData(1, "loc0", "loc2", "loc3", ""),
                                new EventData(5, "loc0", "loc2", "loc3", "")
                        )
                ),
                new TransactionRecord(
                        "trId02",
                        "20151022102011927EDT",
                        "rc02",
                        "cli-02",
                        Arrays.asList(
                                new EventData(1, "loc0", "loc2", "loc3", ""),
                                new EventData(5, "loc0", "loc2", "loc3", "")
                        )
                )
        );
        BatchRequest batchRequest = new BatchRequest(records);

        when(transactionService.processTransactionChunkAsync(any())).thenReturn(CompletableFuture.completedFuture(null));

        eventService.processBatch(batchRequest);

        verify(transactionService,times(1)).processTransactionChunkAsync(any());
    }
    @Test
    @DisplayName("Process batch request fail test")
    void processBatch2() {
        List<TransactionRecord> records = Arrays.asList(
                new TransactionRecord(
                        "trId01",
                        "20151022102011927EDT",
                        "rc01",
                        "cli-01",
                        Arrays.asList(
                                new EventData(1, "loc0", "loc2", "loc3", ""),
                                new EventData(5, "loc0", "loc2", "loc3", "")
                        )
                ),
                new TransactionRecord(
                        "trId02",
                        "20151022102011927EDT",
                        "rc02",
                        "cli-02",
                        Arrays.asList(
                                new EventData(1, "loc0", "loc2", "loc3", ""),
                                new EventData(5, "loc0", "loc2", "loc3", "")
                        )
                )
        );
        BatchRequest batchRequest = new BatchRequest(records);

        when(transactionService.processTransactionChunkAsync(any())).thenThrow(RuntimeException.class);

        assertThrows(DomainException.class,()->eventService.processBatch(batchRequest));

        verify(transactionService,times(1)).processTransactionChunkAsync(any());
    }
}