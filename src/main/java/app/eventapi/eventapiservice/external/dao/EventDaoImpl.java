package app.eventapi.eventapiservice.external.dao;

import app.eventapi.eventapiservice.domain.boundary.EventDao;
import app.eventapi.eventapiservice.domain.dto.EventDto;
import app.eventapi.eventapiservice.external.entity.EventEntity;
import app.eventapi.eventapiservice.external.exception.DbException;
import app.eventapi.eventapiservice.external.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventDaoImpl implements EventDao {
    private final EventRepository eventRepository;
    private final ModelMapper mapper;

    public EventDaoImpl(EventRepository eventRepository, ModelMapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<EventDto> findAllEvents(Pageable pageable) throws DbException {
        try {
            Page<EventEntity> entityPage = eventRepository.findAll(pageable);
            return entityPage.map(entity -> mapper.map(entity, EventDto.class));
        }catch (Exception e){
            throw new DbException("Failed to load events",e);
        }
    }

    @Override
    public Optional<EventDto> findEventById(String id) throws DbException {
        try {
            Optional<EventEntity> event = eventRepository.findById(UUID.fromString(id));
            return event.map(eventEntity -> mapper.map(eventEntity, EventDto.class));
        }catch (DbException e){
            throw new DbException("Failed to load event by Id",e);
        }
    }

    @Override
    public EventDto createEvent(EventDto eventDto) throws DbException {
        try {
            EventEntity event = eventRepository.save(mapper.map(eventDto, EventEntity.class));
            return mapper.map(event, EventDto.class);
        }catch (DbException e){
            throw new DbException("Failed to create event",e);
        }
    }

    @Override
    public EventDto updateEvent(EventDto eventDto) throws DbException {
        try {
            boolean exists = eventRepository.existsById(UUID.fromString(eventDto.getEventId()));
            if (!exists){
                throw new DbException("No event record found with Id");
            }
            EventEntity event = eventRepository.save(mapper.map(eventDto, EventEntity.class));
            return mapper.map(event, EventDto.class);
        }catch (DbException e){
            throw new DbException("Failed to update event",e);
        }
    }

    @Override
    public void deleteEvent(String id) throws DbException {
        try {
            eventRepository.deleteById(UUID.fromString(id));
        }catch (DbException e){
            throw new DbException("Failed to delete event",e);
        }
    }

    @Override
    public void saveAll(List<EventDto> eventEntities) {
        try {
            List<EventEntity> entityList = eventEntities.stream()
                    .map(eventDto -> mapper.map(eventDto, EventEntity.class))
                    .collect(Collectors.toList());
            eventRepository.saveAll(entityList);
        }catch (DbException e){
            throw new DbException("Failed to create event list",e);
        }
    }
}
