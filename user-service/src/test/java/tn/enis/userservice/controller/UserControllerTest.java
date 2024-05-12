//package tn.enis.userservice.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatcher;
//import org.mockito.ArgumentMatchers;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//import tn.enis.userservice.dto.UserRequest;
//import tn.enis.userservice.service.UserService;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doAnswer;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//
//@WebMvcTest(controllers = UserController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    private UserRequest userRequest;
//
//    @BeforeEach
//    void setUp() {
//        UserRequest userRequest = UserRequest.builder()
//                .name("test")
//                .email("test@gmail.com")
//                .password("1234")
//                .build();
//    }
//
//    @Test
//    public void UserController_createUser_ReturnCreated() throws Exception {
//        //arrange
//        String userRequestString = objectMapper.writeValueAsString(userRequest);
//        doAnswer(invocation -> invocation.getArgument(0)).when(userService).saveUser(any());
//        ResultActions response = mockMvc.perform(post("/api/users")
////                .contentType(APPLICATION_JSON_VALUE)
////                .content(userRequestString));
//        response.andExpect(MockMvcResultMatchers.status().isCreated());
//
//        //act
//        //assert
//    }
//
//}