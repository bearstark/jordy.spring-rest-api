package me.jordy.rest.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jordy.rest.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)

// 웹 관련 빈만 등록! 좀 더 빠름.
// 하지만 단위테스트라고 볼 수는 없음.
// 디스패쳐 서블릿, 이벤트 컨트롤러, 핸들러, 매퍼, 컨버터 등등 모두 조합이 된 상태로 동작하는 테스트이기 떄문!
@WebMvcTest
public class EventControllerTest {

    // 스프링 MVC 테스트 핵심 클래스
    // 웹서버를 띄우지 않고도 스프링 MVC 가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임
    // 테스트 크기 : 단위 테스트 < MockMvc < 웹서버 기동 후 테스트
    // 테스트 목록 나열 후 진행
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // 생성 사유
    // 현재 테스트가 @WebMvcTest 로 실행 중.
    // 그래서, EventRepository는 Bean 으로 등록 되지 않음.
    // Mock으로 등록해서 빈이 존재 하지 않음 에러 해결.
    // 하지만 여전히 Null을 반환.
    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(10)
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
                .build()
                ;

        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        ;
    }
}
