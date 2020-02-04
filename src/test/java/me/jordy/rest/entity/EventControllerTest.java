package me.jordy.rest.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())

        ;
    }
}
