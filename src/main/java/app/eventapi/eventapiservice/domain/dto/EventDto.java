package app.eventapi.eventapiservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private String eventId;
    private String transId;
    private String transTms;
    private String rcNum;
    private String clientId;
    private int eventCnt;
    private String locationCd;
    private String locationId1;
    private String locationId2;
    private String addrNbr;
}
