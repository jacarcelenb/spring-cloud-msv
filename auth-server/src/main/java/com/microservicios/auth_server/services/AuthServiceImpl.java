package com.microservicios.auth_server.services;


import com.microservicios.auth_server.dtos.TokenDto;
import com.microservicios.auth_server.dtos.UserDto;
import com.microservicios.auth_server.entities.UserEntity;
import com.microservicios.auth_server.helpers.JwtHelper;
import com.microservicios.auth_server.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
  private UserRepository repository;
  private PasswordEncoder passwordEncoder;
  private static final String USER_EXECPION_MSG = "Error to auth user";
  private final JwtHelper helper;
    @Override
    public TokenDto login(UserDto user) {
        final var userFromDB = this.repository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,USER_EXECPION_MSG));
        this.validPassword(user, userFromDB);
        return TokenDto.builder().accesToken(this.helper.createToken(userFromDB.getUsername())).build();
    }

    @Override
    public TokenDto validToken(TokenDto token) {
        if(this.helper.validateToken(token.getAccesToken())){
           return TokenDto.builder().accesToken(token.getAccesToken()).build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,USER_EXECPION_MSG);
    }


    private void validPassword(UserDto user, UserEntity userEntity){
        if (!this.passwordEncoder.matches(user.getPassword(), userEntity.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,USER_EXECPION_MSG);
        }
    }
}
