package app.eventapi.eventapiservice.domain.service;

import app.eventapi.eventapiservice.application.transport.EventData;
import app.eventapi.eventapiservice.application.transport.TransactionRecord;
import app.eventapi.eventapiservice.domain.boundary.EventDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private EventDao eventDao;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void processTransactionChunkAsync() {
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
                ),
                new TransactionRecord(
                        "trId02",
                        "20151022102011927EDT",
                        "rc02",
                        "cli-02",
                        null
                )
        );

        assertDoesNotThrow(()->transactionService.processTransactionChunkAsync(records));
    }
}