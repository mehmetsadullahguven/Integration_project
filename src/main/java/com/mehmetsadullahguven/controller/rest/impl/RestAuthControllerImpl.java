package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.IRestAuthController;
import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.dto.DtoUser;
import com.mehmetsadullahguven.dto.AuthRequest;
import com.mehmetsadullahguven.dto.AuthResponse;
import com.mehmetsadullahguven.dto.RefreshTokenRequest;
import com.mehmetsadullahguven.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAuthControllerImpl extends RestBaseController implements IRestAuthController {

    @Autowired
    private IAuthService authService;


    @PostMapping("/register")
    @Override
    public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest request) {
        return ok(authService.register(request), "register success");
    }

    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ok(authService.authenticate(request), "authenticate success");
    }

    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ok(authService.refreshToken(request), "refresh token success");
    }
}
