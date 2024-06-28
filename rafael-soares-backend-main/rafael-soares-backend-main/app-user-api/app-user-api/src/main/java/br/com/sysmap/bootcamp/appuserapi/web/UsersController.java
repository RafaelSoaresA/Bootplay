package br.com.sysmap.bootcamp.appuserapi.web;

import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.service.UsersService;
import br.com.sysmap.bootcamp.appuserapi.dto.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/create")
    @Operation(summary = "Save user", description = "Function responsible for user register.")
    public ResponseEntity<Users> saveUser (@RequestBody Users user) {
        return ResponseEntity.ok(usersService.create(user));
    }

    @PutMapping("/update")
    @Operation(summary = "Update user", description = "Function responsible for update user data.")
    public ResponseEntity<Users> updateUser (@RequestBody Users user) {
        return ResponseEntity.ok().body(usersService.update(user));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get user by ID", description = "Function responsible for search the user by the ID.")
    public ResponseEntity<Users> getUser (@PathVariable Long id) {
        Users user = usersService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/auth")
    @Operation(summary = "Auth user", description = "Function responsible to generate an authentication token, to user access.")
    public ResponseEntity<AuthDto> authUser (@RequestBody AuthDto authDto) {
        //AuthDto user = usersService.auth(authDto);
        return ResponseEntity.ok().body(usersService.auth(authDto));
    }

    @GetMapping("/")
    @Operation(summary = "Get users", description = "Function responsible to get all users registered in the database.")
    public ResponseEntity<List<Users>> getAllUsers () {
        List<Users> users = usersService.listUsers();
        return ResponseEntity.ok(users);
    }



}
