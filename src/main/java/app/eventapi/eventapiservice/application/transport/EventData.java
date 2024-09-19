package app.eventapi.eventapiservice.application.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private int eventCnt;
    private String locationCd;
    private String locationId1;
    private String locationId2;
    private String addrNbr;
}
