package com.bilyoner.assignment.couponapi.controller;

import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.EventDTO;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.service.CouponService;
import com.bilyoner.assignment.couponapi.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private EventService eventService;

    @MockBean
    private CouponService couponService;



    @Test
    public void getAllEvents_thenReturns200() throws Exception {
        EventDTO eventDTO = EventDTO.builder().id(1l).build();
        List<EventDTO> eventDTOList = Arrays.asList(eventDTO);
        doReturn(eventDTOList).when(eventService).getAllEvents();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/event/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(eventDTO.getId().intValue())));
    }

    @Test
    public void getEvent_thenReturns200() throws Exception {
        EventDTO eventDTO = EventDTO.builder().id(1l).build();
        doReturn(eventDTO).when(eventService).getEventById(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/event/{id}",1l))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventDTO.getId().intValue())));
    }

    @Test
    public void createEvent_thenReturns200() throws Exception {
        EventDTO eventDTO = EventDTO.builder().id(1l).mbs(3).name("test").build();
        doReturn(eventDTO).when(eventService).createEvent(any(EventDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventDTO.getId().intValue())));
    }


}
