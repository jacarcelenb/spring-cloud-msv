package com.microservicios.auth_server.services;

import com.microservicios.auth_server.dtos.TokenDto;
import com.microservicios.auth_server.dtos.UserDto;

public interface AuthService {

    TokenDto login(UserDto user);
    TokenDto validToken( TokenDto token);
}
