package com.penekhun.ctfjserver.User.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.forTest.MultiValueMapConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class LoginOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final AccountDto.Req.Signup signup =
            AccountDto.Req.Signup.builder()
            .username("test9838test")
            .password("test9838test")
            .nickname("TestAccount9838")
            .email("testaccount@test.com")
            .realName("홍길동")
            .build();

    @Test
    void 테스트로그인_성공() throws Exception {
        //todo: dto 변경
        MultiValueMap<String, String> multiValueSignup = MultiValueMapConverter.convert(new ObjectMapper(), signup);

        mockMvc.perform(
                post("/api/v1/login").params(multiValueSignup))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andDo(print());
    }



    @Test
    void 테스트로그인_실패_찾을수없는멤버() throws Exception {
        //todo: dto 변경
        MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<>();
        loginParams.add("username", "sifjicmzxwci21");
        loginParams.add("password", "password111111");

        mockMvc.perform(post("/api/v1/login").params(loginParams)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("errorCode").value(ErrorCode.MEMBER_NOT_FOUND.getErrorCode()));
        //  .andExpect()

    }

    @Test
    void 테스트로그아웃() {

    }

    @Test
    @BeforeEach
    void 테스트임시_회원가입_컨트롤러() throws Exception {
        MultiValueMap<String, String> multiValueSignup = MultiValueMapConverter.convert(new ObjectMapper(), signup);

        mockMvc.perform(
                post("/api/v1/tempSignup").params(multiValueSignup))
                .andExpect(status().isOk())
                .andDo(print());
    }

}