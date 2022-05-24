package com.penekhun.ctfjserver.User.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.forTest.MultiValueMapConverter;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@DataJpaTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("token")))
                .andExpect(content().string(containsString("tokenExpired")))
                .andExpect(content().string(containsString("refresh")))
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
                .andExpect(content().string(containsString(ErrorCode.MEMBER_NOT_FOUND.getErrorCode())));
        //  .andExpect()

    }

    @Test
    void 테스트로그아웃() {

    }

    @Test
    @BeforeAll
    @AfterAll
    void 계정_삭제() throws Exception {
        Optional<Account> account = accountRepository.findByUsername(signup.getUsername());
        if (account.isPresent())
            accountRepository.delete(account.get());
        else Assertions.assertTrue(account.isEmpty());

        account = accountRepository.findByUsername(signup.getUsername());
        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    @BeforeAll
    public void 테스트임시_회원가입_컨트롤러() throws Exception {
        MultiValueMap<String, String> multiValueSignup = MultiValueMapConverter.convert(new ObjectMapper(), signup);

        mockMvc.perform(
                post("/api/v1/tempSignup").params(multiValueSignup))
                .andExpect(status().isOk())
                .andDo(print());
    }


}