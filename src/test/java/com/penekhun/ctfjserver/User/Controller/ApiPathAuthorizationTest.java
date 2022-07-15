package com.penekhun.ctfjserver.User.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ApiPathAuthorizationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_존재하지않는페이지_접근() throws Exception{
        mvc.perform(post("/api/v1/admin")
                        .characterEncoding("UTF-8")
                        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_계정페이지_접근() throws Exception{
        mvc.perform(post("/api/v1/admin/account")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_공지페이지_접근() throws Exception{
        mvc.perform(post("/api/v1/admin/notice")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_알림페이지_접근() throws Exception{
        mvc.perform(post("/api/v1/admin/notification")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_문제페이지_접근() throws Exception{
        mvc.perform(post("/api/v1/admin/problem")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_파일페이지_접근() throws Exception{
        mvc.perform(post("/api/v1/admin/file")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 유저가_어드민_로그페이지_접근() throws Exception{
        mvc.perform(get("/api/v1/admin/log")
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isForbidden());
    }

}