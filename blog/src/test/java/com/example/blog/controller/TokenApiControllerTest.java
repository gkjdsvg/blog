//package com.example.blog.controller;
//
//import com.example.blog.DTO.CreateAccessTokenRequest;
//import com.example.blog.domain.RefreshToken;
//import com.example.blog.domain.User;
//import com.example.blog.jwt.JwtFactory;
//import com.example.blog.jwt.JwtProperties;
//import com.example.blog.repository.RefreshTokenRepository;
//import com.example.blog.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//
//class TokenApiControllerTest {
//    @Autowired
//    protected MockMvc mockMvc;
//    @Autowired
//    protected ObjectMapper objectMapper;
//    @Autowired
//    protected WebApplicationContext context;
//    @Autowired
//    JwtProperties jwtProperties;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    @Test
//    void testBeanInjection() {
//        assertNotNull(jwtProperties);  // 빈이 주입되지 않으면 null
//        assertNotNull(userRepository);  // 빈이 주입되지 않으면 null
//    }
//
//    @BeforeEach
//    public void setup() {
//            this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();
//        userRepository.deleteAll();
//    }
//
//    @DisplayName("Create New Access Token: 새로운 액세스 토큰 발급")
////    @WithMockUser(username = "testUser", roles = "USER")  // 가짜 사용자 인증
//    @Test
//    public void createNewAccessToken() throws Exception {
//        final String url = "/api/token";
//
//        User testUser = userRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        String refreshToken = JwtFactory.builder()
//                .claims(Map.of("id", testUser.getId()))
//                .build()
//                .createToken(jwtProperties);
//
//        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));
//
//        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
//        request.setRefreshToken(refreshToken);
//        final String requestBody = objectMapper.writeValueAsString(request);
//
//        ResultActions resultActions = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(requestBody));
//
//        resultActions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty());
//
//    }
//}