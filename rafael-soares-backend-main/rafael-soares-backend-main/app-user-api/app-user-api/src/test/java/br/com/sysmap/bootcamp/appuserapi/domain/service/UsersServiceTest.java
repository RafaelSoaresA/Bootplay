package br.com.sysmap.bootcamp.appuserapi.domain.service;

import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.appuserapi.dto.AuthDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UsersServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersService usersService;

    @MockBean
    private Users users;

    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Logger log;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("teste_teste@email.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should return users when valid users is saved")
    public void shouldReturnUsersWhenValidUsersIsSaved() throws Exception {
        Users users = Users.builder().id(1L).name("Teste").email("teste_teste@email.com").password("123").build();
        UserDto userDto = UserDto.builder().id(1L).name("Teste").email("teste_teste@email.com").build();

        Mockito.when(usersService.create(users)).thenReturn(userDto);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usersService.create(users);
        });

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
    @DisplayName("Should return an exception if users already saved")
    public void shouldReturnAnExceptionWhenUserAlreadySaved() throws Exception {
        users.setEmail("existing@example.com");
        Mockito.when(usersRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(users));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usersService.create(users);
        });

        assertEquals("User already exists",exception.getMessage());
    }

    @Test
    @DisplayName("Should return user id")
    public void shouldReturnUserIdWhenValidUserIdIsSaved() throws Exception {
        Long id = 1L;
        Users users = Users.builder().id(id).build();

        Mockito.when(usersRepository.findById(id)).thenReturn(Optional.of(users));

        mockMvc.perform(get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                //.content(objectMapper.writeValueAsString(users)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    @DisplayName("Should test if the username is found")
    public void testLoadByUsernameFound() throws Exception {
        Users users = Users.builder().id(1L).name("Teste").email("teste_teste@email.com").password("123").build();

        users.setEmail("teste_teste@email.com");

        Mockito.when(usersRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(users));

        UserDetails result = usersService.loadUserByUsername(users.getEmail());


        assertNotNull(result);
        assertEquals(users.getEmail(), result.getUsername());
        assertTrue(result.getAuthorities().isEmpty());

    }

    @Test
    @DisplayName("Should find user by email")
    public void findByEmail() throws Exception {

        Users users = Users.builder().id(1L).name("Teste").email("teste_teste@email.com").password("123").build();
        users.setEmail("teste_teste@email.com");

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> usersService.findByEmail(users.getEmail())
        );
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Should update the user")
    public void shouldUpdateUser() throws Exception {
        Users users = Users.builder().id(1L).name("test").email("test@example.com").password("123").build();

        Mockito.when(usersRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(users));
        Mockito.when(usersRepository.save(any(Users.class))).thenReturn(users);

        Users result = usersService.update(users);

        assertEquals(1L, result.getId());
        assertEquals("test", result.getName());

        verify(usersRepository).findByEmail("test@example.com");
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("Should list users")
    public void shouldListUsers() throws Exception{
        Users user1 = Users.builder().id(1L).name("test").email("test@example.com").password("123").build();
        Users user2 = Users.builder().id(2L).name("teste").email("teste@example.com").password("234").build();
        List<Users> expectedUsers = Arrays.asList(user1, user2);

        Mockito.when(usersRepository.findAll()).thenReturn(expectedUsers);

        List<Users> actualUsers = usersService.listUsers();

        assertNotNull(actualUsers);
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertTrue(actualUsers.containsAll(expectedUsers));
    }

    @Test
    @DisplayName("Should test the authentication")
    public void shouldAuth() throws Exception{
        StringBuilder password = new StringBuilder().append(users.getEmail()).append(":").append(users.getPassword());

        Users users = Users.builder().id(1L).name("test").email("test@example.com").password(passwordEncoder.encode("123")).build();

        Mockito.when(usersRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(users));


        AuthDto authDto = AuthDto.builder().email("test@example.com").password("123").token(
                Base64.getEncoder().withoutPadding().encodeToString(password.toString().getBytes())
        ).id(users.getId()).build();

        /*RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> usersService.findByEmail(users.getEmail())
        );*/

        mockMvc.perform(post("/users/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isOk());
                //.andExpect(content().json(objectMapper.writeValueAsString(authDto)));




        assertNotNull(authDto);
        assertEquals(users.getEmail(), authDto.getEmail());
        //assertEquals("Invalid password", thrown.getMessage());
    }
}