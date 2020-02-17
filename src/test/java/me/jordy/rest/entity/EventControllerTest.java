package me.jordy.rest.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jordy.rest.BaseControllerTest;
import me.jordy.rest.common.RestDocsConfiguration;
import me.jordy.rest.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Rest API Development Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,1,1, 13,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,01,10,13,0))
                .beginEventDateTime(LocalDateTime.of(2020,1,13,13,0))
                .endEventDateTime(LocalDateTime.of(2020,1,13,15,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .free(true)
                .offline(true)
                .build()
                ;

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
//                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("createEvent",
                        links( // 링크 문서화
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update events"),
                                linkWithRel("profile").description("link to profile events")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content=type header")
                        ),
                        requestFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
                        ),
                        // 1) links 내 self, query-events, update-event를 문서화 대상에서 제외하기 위해
                        //     relaxed란 접두사를 붙임.
                        // 2) 포함 시키는 것도 가능.
                        responseFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));

//                .andExpect(jsonPath("id").value(Matchers.not(100)))
//                .andExpect(jsonPath("free").value(Matchers.not(true)))
        ;
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Development Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,2,1, 13,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,1,10,13,0))
                .beginEventDateTime(LocalDateTime.of(2020,1,13,13,0))
                .endEventDateTime(LocalDateTime.of(2020,1,13,15,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .build()
                ;

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Development Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,2,1, 13,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,1,10,13,0))
                .beginEventDateTime(LocalDateTime.of(2020,1,13,13,0))
                .endEventDateTime(LocalDateTime.of(2020,1,13,15,0))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .build()
                ;

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].field").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("content[0].rejectedValue").exists()) // 별 달리 안 묶이면 $(루트)로 사용
                .andExpect(jsonPath("_links.index").exists()) // 에러 있을 때 인덱스로 가e게끔 유도.
        ;
    }

    @Test
    public void queryEvents() throws Exception {
        IntStream.range(0,30).forEach(this::generateEvent);

        // When
        ResultActions perform = this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","name,DESC"));

        // Then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
        ;
    }

    @Test
    public void getEvent() throws Exception {

        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))

        ;
    }

    @Test
    public void getInvalidEvent() throws Exception {

        // When & Then
        this.mockMvc.perform(get("/api/events/12516"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        String changedEventName = "수정된 이름";
        eventDto.setName(changedEventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto))
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(changedEventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    public void updateEvent400_Empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
//                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void updateEvent400_Wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
//                .accept(MediaType.APPLICATION_JSON_UTF8)
                 )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/12315153")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
//                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("스프링")
                .description("Rest API Development Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,1,1, 13,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,01,10,13,0))
                .beginEventDateTime(LocalDateTime.of(2020,1,13,13,0))
                .endEventDateTime(LocalDateTime.of(2020,1,13,15,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .free(false)
                .offline(true)
                .build()
                ;
        return this.eventRepository.save(event);
    }
}
