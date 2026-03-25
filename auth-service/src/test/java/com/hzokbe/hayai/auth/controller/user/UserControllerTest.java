package com.hzokbe.hayai.auth.controller.user;

import com.hzokbe.hayai.auth.config.security.SecurityConfig;
import com.hzokbe.hayai.auth.dto.user.SignInRequestDTO;
import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.exception.user.EmailAlreadyInUseException;
import com.hzokbe.hayai.auth.exception.user.UserIsNotActiveException;
import com.hzokbe.hayai.auth.exception.user.UserNotFoundException;
import com.hzokbe.hayai.auth.exception.user.UsernameAlreadyInUseException;
import com.hzokbe.hayai.auth.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    public void signUp_shouldThrowException_whenUsernameAlreadyInUse() throws Exception {
        doThrow(new UsernameAlreadyInUseException("username already in use")).when(service).signUp(any(SignUpRequestDTO.class));

        var request = post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"ayumu_kasuga\", \"email\": \"ayumu_kasuga@azumangadaioh.jp\", \"password\": \"ayumu!123\"}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("username already in use"));
    }

    @Test
    public void signUp_shouldThrowException_whenEmailAlreadyInUse() throws Exception {
        doThrow(new EmailAlreadyInUseException("e-mail already in use")).when(service).signUp(any(SignUpRequestDTO.class));

        var request = post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"ayumu_kasuga\", \"email\": \"ayumu_kasuga@azumangadaioh.jp\", \"password\": \"ayumu!123\"}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("e-mail already in use"));
    }

    @Test
    public void signUp_shouldSaveUser() throws Exception {
        var request = post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"ayumu_kasuga\", \"email\": \"ayumu_kasuga@azumangadaioh.jp\", \"password\": \"ayumu!123\"}");

        mockMvc.perform(request).andExpect(status().isCreated());
    }

    @Test
    public void signIn_shouldThrowException_whenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("user not found")).when(service).signIn(any(SignInRequestDTO.class));

        var request = post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"ayumu_kasuga\", \"password\": \"ayumu!123\"}");

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    public void signIn_shouldThrowException_whenUserIsNotActive() throws Exception {
        doThrow(new UserIsNotActiveException("user is not active")).when(service).signIn(any(SignInRequestDTO.class));

        var request = post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"ayumu_kasuga\", \"password\": \"ayumu!123\"}");

        mockMvc.perform(request).andExpect(status().isUnauthorized());
    }
}
