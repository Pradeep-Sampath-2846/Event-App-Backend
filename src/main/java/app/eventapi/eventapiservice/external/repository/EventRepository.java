package app.eventapi.eventapiservice.external.repository;

import app.eventapi.eventapiservice.external.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {
}
