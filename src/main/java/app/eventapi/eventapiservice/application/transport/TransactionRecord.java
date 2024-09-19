package app.eventapi.eventapiservice.application.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@AllArgsConstructor@NoArgsConstructor
public class TransactionRecord {
    private String transId;
    private String transTms;
    private String rcNum;
    private String clientId;
    private List<EventData> event;
}
