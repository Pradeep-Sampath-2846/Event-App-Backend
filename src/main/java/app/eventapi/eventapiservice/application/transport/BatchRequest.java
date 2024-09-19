package app.eventapi.eventapiservice.application.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchRequest {
    private List<TransactionRecord> records;
}
