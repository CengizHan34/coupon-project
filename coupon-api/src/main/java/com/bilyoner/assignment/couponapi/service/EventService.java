package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.EventDTO;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<EventDTO> getAllEvents() {
        final List<EventEntity> eventEntity = eventRepository.findAll();
        return eventEntity.stream().map(event -> EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .mbs(event.getMbs())
                .type(event.getType())
                .eventDate(event.getEventDate()).build()).collect(Collectors.toList());
    }

    public EventDTO createEvent(EventDTO eventRequest) {
        final EventEntity createdEventEntity = eventRepository.save(EventEntity.builder()
                .name(eventRequest.getName())
                .mbs(eventRequest.getMbs())
                .type(eventRequest.getType())
                .eventDate(eventRequest.getEventDate())
                .build());

        final EventDTO response = EventDTO.mapToEventDTO(createdEventEntity);

        return response;
    }

    public List<EventEntity> findByIds(List<Long> eventIds){
        final List<EventEntity> eventEntities = eventRepository.findByIdIn(eventIds);
        return eventEntities;
    }

    public EventDTO getEventById(Long id) {
        final EventEntity eventEntity = eventRepository.findById(id).get();
        EventDTO eventDTO = EventDTO.mapToEventDTO(eventEntity);
        return eventDTO;
    }
}
