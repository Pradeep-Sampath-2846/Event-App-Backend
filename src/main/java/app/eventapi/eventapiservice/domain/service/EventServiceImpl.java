package app.eventapi.eventapiservice.domain.service;

import app.eventapi.eventapiservice.application.transport.*;
import app.eventapi.eventapiservice.domain.boundary.EventDao;
import app.eventapi.eventapiservice.domain.boundary.EventService;
import app.eventapi.eventapiservice.domain.dto.EventDto;
import app.eventapi.eventapiservice.domain.exception.DomainException;
import app.eventapi.eventapiservice.domain.util.ChunkUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private final TransactionService transactionService;
    private final ModelMapper mapper;
    private final   int chunkSize;

    public EventServiceImpl(
            EventDao eventDao,
            TransactionService transactionService, ModelMapper mapper,
            @Value("${service.event.batch.chunk-size}") int chunkSize) {
        this.eventDao = eventDao;
        this.transactionService = transactionService;
        this.mapper = mapper;
        this.chunkSize = chunkSize;
    }

    @Override
    public Page<EventDto> getAllEvents(Pageable page) throws DomainException {
        try {
            return eventDao.findAllEvents(page);
        }catch (Exception e){
            throw new DomainException("Failed to load the data",e);
        }
    }

    @Override
    public Optional<EventDto> getEventById(String id) throws DomainException {
        try {
            return eventDao.findEventById(id);
        }catch (Exception e){
            throw new DomainException("Failed to load event data",e);
        }
    }

    @Override
    public EventDto createEvent(EventCreateRequest request) throws DomainException {
        try {
            EventDto eventDto = mapper.map(request, EventDto.class);
            return eventDao.createEvent(eventDto);
        }catch (Exception e){
            throw new DomainException("Failed to create event",e);
        }
    }

    @Override
    public EventDto updateEvent(String id, EventUpdateRequest request) throws DomainException {
        try{
            EventDto eventDto = mapper.map(request, EventDto.class);
            eventDto.setEventId(id);
            return eventDao.updateEvent(eventDto);
        }catch (Exception e){
            throw new DomainException("Failed to update event",e);
        }
    }

    @Override
    public void deleteEvent(String id) throws DomainException {
        try {
            eventDao.deleteEvent(id);
        }catch (Exception e){
            throw new DomainException("Failed to delete the event",e);
        }
    }

    @Override
    public void processBatch(BatchRequest batchRequest) throws DomainException {
        try {
            List<TransactionRecord> records = batchRequest.getRecords();
            List<List<TransactionRecord>> chunkedList = ChunkUtils.createChunkedList(records, chunkSize);

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (List<TransactionRecord> chunk : chunkedList) {
                futures.add(transactionService.processTransactionChunkAsync(chunk));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }catch (Exception e){
            throw new DomainException("Failed to process the event batch request");
        }

    }
}
