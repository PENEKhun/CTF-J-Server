package com.penekhun.ctfjserver.User.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penekhun.ctfjserver.Config.SecurityConfig;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.forTest.MultiValueMapConverter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import javax.annotation.security.RunAs;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class LoginOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 테스트로그인() {

    }

    @Test
    void 테스트로그아웃() {
    }

    @Test
    void 테스트임시_회원가입_컨트롤러() throws Exception {
        AccountDto.Req.Signup signup= AccountDto.Req.Signup.builder()
                                    .username("test9838test")
                                    .password("test9838test")
                                    .nickname("TestAccount9838")
                                    .email("testaccount@test.com")
                                    .realName("홍길동")
                                    .build();

        MultiValueMap<String, String> multiValueSignup = MultiValueMapConverter.convert(new ObjectMapper(), signup);

        mockMvc.perform(
                post("/api/v1/tempSignup").params(multiValueSignup))
                .andExpect(status().isOk())
                .andDo(print());

    }
}