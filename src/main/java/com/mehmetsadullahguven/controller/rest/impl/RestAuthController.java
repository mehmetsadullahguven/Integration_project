package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.dto.auth.DtoUser;
import com.mehmetsadullahguven.dto.auth.AuthRequest;
import com.mehmetsadullahguven.dto.auth.AuthResponse;
import com.mehmetsadullahguven.dto.auth.RefreshTokenRequest;
import com.mehmetsadullahguven.service.rest.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAuthController extends RestBaseController {

    @Autowired
    private IAuthService authService;


    @PostMapping("/register")
    public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest request) {
        return ok(authService.register(request), "register success");
    }

    @PostMapping("/authenticate")
    public RootEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ok(authService.authenticate(request), "authenticate success");
    }

    @PostMapping("/refreshToken")
    public RootEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ok(authService.refreshToken(request), "refresh token success");
    }
}
