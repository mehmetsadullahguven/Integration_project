package com.mehmetsadullahguven.service;

import com.mehmetsadullahguven.dto.DtoUser;
import com.mehmetsadullahguven.dto.AuthRequest;
import com.mehmetsadullahguven.dto.AuthResponse;
import com.mehmetsadullahguven.dto.RefreshTokenRequest;
import com.mehmetsadullahguven.model.RefreshToken;
import com.mehmetsadullahguven.model.User;

public interface IAuthService {

    public DtoUser register(AuthRequest request);

    public AuthResponse authenticate(AuthRequest request);

    public RefreshToken saveRefreshToken(User user);

    public AuthResponse refreshToken(RefreshTokenRequest request);

}
