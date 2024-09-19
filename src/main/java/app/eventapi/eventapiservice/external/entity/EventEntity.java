package app.eventapi.eventapiservice.external.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @Column(name = "event_id",columnDefinition = "UUID", nullable = false, updatable = false)
    @GeneratedValue
    private UUID eventId;

    @Column(name = "trans_id",length = 50)
    private String transId;

    @Column(name = "trans_tms",length = 50)
    private String transTms;

    @Column(name = "rc_num",length = 50)
    private String rcNum;

    @Column(name = "client_id",length = 50)
    private String clientId;

    @Column(name = "event_cnt")
    private int eventCnt;

    @Column(name = "location_cd",length = 50)
    private String locationCd;

    @Column(name = "location_id1",length = 50)
    private String locationId1;

    @Column(name = "location_id2",length = 50)
    private String locationId2;

    @Column(name = "addr_nbr",length = 50)
    private String addrNbr;


}
