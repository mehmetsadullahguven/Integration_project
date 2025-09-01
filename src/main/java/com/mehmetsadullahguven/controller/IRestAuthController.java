package com.mehmetsadullahguven.controller;

import com.mehmetsadullahguven.dto.DtoUser;
import com.mehmetsadullahguven.dto.AuthRequest;
import com.mehmetsadullahguven.dto.AuthResponse;
import com.mehmetsadullahguven.dto.RefreshTokenRequest;

public interface IRestAuthController {

    public RootEntity<DtoUser> register(AuthRequest request);

    public RootEntity<AuthResponse> authenticate(AuthRequest request);

    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest request);

}
