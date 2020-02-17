package me.jordy.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jordy.rest.common.RestDocsConfiguration;
import me.jordy.rest.repository.EventRepository;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // RestDocs 자동 설정
@Import(RestDocsConfiguration.class) // 설정 파일 주입
@Ignore
public class BaseControllerTest {

    // 스프링 MVC 테스트 핵심 클래스
    // 웹서버를 띄우지 않고도 스프링 MVC 가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임
    // 테스트 크기 : 단위 테스트 < MockMvc < 웹서버 기동 후 테스트
    // 테스트 목록 나열 후 진행
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;
}
