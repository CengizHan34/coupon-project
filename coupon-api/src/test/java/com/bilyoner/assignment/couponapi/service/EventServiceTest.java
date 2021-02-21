package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.EventDTO;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    private EventService eventService;

    @BeforeEach
    void setUp(){
        eventService = new EventService(eventRepository);
    }

    @Test
    public void getAllEvents_IfGetAllEventsIsCalled_allEventsMustReturn(){
        EventEntity eventEntity = EventEntity.builder()
                .id(1l).eventDate(LocalDateTime.now()).name("Test - Test").type(EventTypeEnum.FOOTBALL).mbs(3)
                .build();

        List<EventEntity> eventEntities = new ArrayList<>();
        eventEntities.add(eventEntity);
        when(eventRepository.findAll()).thenReturn(eventEntities);
        List<EventDTO> eventDTOS = eventService.getAllEvents();
        Assertions.assertNotNull(eventDTOS);
    }

    @Test
    public void createEvent_WhenCreateEventIsCalled_shouldReturnEventDto(){
        EventDTO eventDTORequest = EventDTO.builder().name("TEST").eventDate(LocalDateTime.now())
                .type(EventTypeEnum.FOOTBALL).mbs(3).build();

        EventEntity eventEntity = EventEntity.builder()
                .id(1l).eventDate(LocalDateTime.now()).name("Test - Test").type(EventTypeEnum.FOOTBALL).mbs(3)
                .build();
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        EventDTO eventDTO = eventService.createEvent(eventDTORequest);
        Assertions.assertNotNull(eventDTO);
        Assertions.assertEquals(1l,eventDTO.getId());
    }

    @Test
    public void findByIds_IfFindByIdsIsCalled_eventsShouldReturnAccordingToTheId(){
        List<Long> ids = Arrays.asList(1l,2l);
        EventEntity eventEntity1 = EventEntity.builder()
                .id(1l).eventDate(LocalDateTime.now()).name("Test - Test").type(EventTypeEnum.FOOTBALL).mbs(3)
                .build();
        EventEntity eventEntity2 = EventEntity.builder()
                .id(2l).eventDate(LocalDateTime.now()).name("Test - Test").type(EventTypeEnum.FOOTBALL).mbs(2)
                .build();

        List<EventEntity> eventEntities = Arrays.asList(eventEntity1,eventEntity2);

        when(eventService.findByIds(ids)).thenReturn(eventEntities);

        List<EventEntity> eventEntityList = eventService.findByIds(ids);
        Assertions.assertNotNull(eventEntityList);
    }

    @Test
    public void getAllEvents_IfGetAllEventsIsCalled_shouldReturnAllEvents(){
        EventEntity eventEntity1 = EventEntity.builder()
                .id(1l).eventDate(LocalDateTime.now()).name("Test - Test").type(EventTypeEnum.FOOTBALL).mbs(3)
                .build();
        Optional<EventEntity> optionalEventEntity = Optional.of(eventEntity1);
        when(eventRepository.findById(any(Long.class))).thenReturn(optionalEventEntity);

        EventDTO eventDTO = eventService.getEventById(any(Long.class));
        Assertions.assertNotNull(eventDTO);
    }
}
