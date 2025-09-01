package com.mehmetsadullahguven.controller.impl;

import com.mehmetsadullahguven.controller.IRestAuthController;
import com.mehmetsadullahguven.controller.RestBaseController;
import com.mehmetsadullahguven.controller.RootEntity;
import com.mehmetsadullahguven.dto.DtoUser;
import com.mehmetsadullahguven.dto.AuthRequest;
import com.mehmetsadullahguven.dto.AuthResponse;
import com.mehmetsadullahguven.dto.RefreshTokenRequest;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
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
