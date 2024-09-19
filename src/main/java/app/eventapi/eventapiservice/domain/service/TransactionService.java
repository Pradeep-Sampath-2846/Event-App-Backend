package app.eventapi.eventapiservice.domain.service;

import app.eventapi.eventapiservice.application.transport.EventData;
import app.eventapi.eventapiservice.application.transport.TransactionRecord;
import app.eventapi.eventapiservice.domain.boundary.EventDao;
import app.eventapi.eventapiservice.domain.dto.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TransactionService {
    private final EventDao eventDao;

    public TransactionService(EventDao eventDao) {
        this.eventDao = eventDao;
    }
    @Async("taskExecutor")
    public CompletableFuture<Void> processTransactionChunkAsync(List<TransactionRecord> chunk) {

        List<EventDto> eventEntities = new ArrayList<>();
        // Processing the chunk
        for (TransactionRecord record : chunk) {
            if (Objects.isNull(record.getEvent())) continue;
            for (EventData event : record.getEvent()) {
                EventDto eventDto = EventDto.builder()
                        .eventCnt(event.getEventCnt())
                        .locationCd(event.getLocationCd())
                        .locationId1(event.getLocationId1())
                        .locationId2(event.getLocationId2())
                        .addrNbr(event.getAddrNbr())
                        .transId(record.getTransId())
                        .transTms(record.getTransTms())
                        .rcNum(record.getRcNum())
                        .clientId(record.getClientId())
                        .build();

                eventEntities.add(eventDto);
            }
        }

        eventDao.saveAll(eventEntities);

        return CompletableFuture.completedFuture(null);

    }
}
