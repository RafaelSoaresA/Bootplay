package br.com.sysmap.bootcamp.appuserapi.web;
import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.appuserapi.domain.service.UsersService;
import br.com.sysmap.bootcamp.appuserapi.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersService usersService;

    @MockBean
    private UsersRepository usersRepository;

    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
    }

    @Test
    @DisplayName("Should return users when valid users is saved")
    public void shouldReturnUsersWhenValidUsersIsSaved() throws Exception {
        Users users = Users.builder().id(1L).name("Teste").email("teste_teste@email.com").password("123").build();
        UserDto userDto = UserDto.builder().id(1L).name("Teste").email("teste_teste@email.com").build();

        Mockito.when(usersService.create(users)).thenReturn(userDto);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(status().isOk())
                /*.andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Teste"))
                .andExpect(jsonPath("$.email").value("teste_teste@email.com"));*/
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @DisplayName("Should return user id")
    public void shouldReturnUserIdWhenValidUserIdIsSaved() throws Exception {
        Long id = 1L;
        Users users = Users.builder().id(id).build();
        UserDto userDto = UserDto.builder().id(id).build();

        Mockito.when(usersRepository.findById(id)).thenReturn(Optional.of(users));

        mockMvc.perform(get("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                //.content(objectMapper.writeValueAsString(users)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    @DisplayName("Should test the update path")
    public void shouldUpdate() throws Exception {
        Users users = Users.builder().id(1L).name("test").email("test@example.com").password("123").build();

        Mockito.when(usersRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(users));
        Mockito.when(usersRepository.save(any(Users.class))).thenReturn(users);

        Users result = usersService.update(users);

        mockMvc.perform(put ("/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(users)))
                        .andExpect(status().isOk());


        assertEquals(1L, result.getId());
        assertEquals("test", result.getName());

    }
}
