package br.com.sysmap.bootcamp.appuserapi.domain.service;

import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.entities.Wallet;
import br.com.sysmap.bootcamp.appuserapi.domain.repository.UsersRepository;
import br.com.sysmap.bootcamp.appuserapi.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.appuserapi.dto.AuthDto;
import br.com.sysmap.bootcamp.appuserapi.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto create(Users user) {
        Optional<Users> usersOptional = this.usersRepository.findByEmail(user.getEmail());
        if (usersOptional.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        user = user.toBuilder().password(passwordEncoder.encode(user.getPassword())).build();

        log.info("Creating user: {}", user);
        try {
            this.usersRepository.save(user);
            return modelMapper.map(user, UserDto.class);
        }
        catch (Exception e) {
           log.error("Error saving user: {}", e);
        }
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> usersOptional = this.usersRepository.findByEmail(username);

        return usersOptional.map(users -> new User(users.getEmail(), users.getPassword(), new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }

    public Users findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Users findById(Long id){
        Optional<Users> usersOptional = this.usersRepository.findById(id);
        log.info("Get user by ID: {}", id);
        return usersOptional.get();
    }

    public Users update(Users user) {
       Optional<Users> usersOptional = this.usersRepository.findByEmail(user.getEmail());

       user = user.toBuilder().password(passwordEncoder.encode(user.getPassword())).build();
       user.setId(usersOptional.get().getId());
       user.setName(usersOptional.get().getName());
       return this.usersRepository.save(user);
    }

    public List<Users> listUsers(){
        return usersRepository.findAll();
    }

    public AuthDto auth(AuthDto authDto) {
        Users users = this.findByEmail(authDto.getEmail());

        if (!this.passwordEncoder.matches(authDto.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        StringBuilder password = new StringBuilder().append(users.getEmail()).append(":").append(users.getPassword());

        return AuthDto.builder().email(users.getEmail()).token(
                Base64.getEncoder().withoutPadding().encodeToString(password.toString().getBytes())
        ).id(users.getId()).build();
    }

}
