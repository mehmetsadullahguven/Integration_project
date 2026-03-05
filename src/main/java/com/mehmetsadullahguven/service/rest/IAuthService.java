package com.mehmetsadullahguven.service.rest;

import com.mehmetsadullahguven.dto.auth.DtoUser;
import com.mehmetsadullahguven.dto.auth.AuthRequest;
import com.mehmetsadullahguven.dto.auth.AuthResponse;
import com.mehmetsadullahguven.dto.auth.RefreshTokenRequest;
import com.mehmetsadullahguven.model.RefreshToken;
import com.mehmetsadullahguven.model.User;

public interface IAuthService {

    public DtoUser register(AuthRequest request);

    public AuthResponse authenticate(AuthRequest request);

    public RefreshToken saveRefreshToken(User user);

    public AuthResponse refreshToken(RefreshTokenRequest request);

}
