package com.mehmetsadullahguven.service.impl;

import com.mehmetsadullahguven.dto.DtoUser;
import com.mehmetsadullahguven.dto.RefreshTokenRequest;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.RefreshToken;
import com.mehmetsadullahguven.model.User;
import com.mehmetsadullahguven.dto.AuthRequest;
import com.mehmetsadullahguven.dto.AuthResponse;
import com.mehmetsadullahguven.jwt.JwtService;
import com.mehmetsadullahguven.repository.IRefreshTokenRepository;
import com.mehmetsadullahguven.repository.IUserRepository;
import com.mehmetsadullahguven.service.IAuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationProvider.authenticate(authRequest);
            Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
            String accessToken = jwtService.generateJwtToken(optionalUser.get());
            RefreshToken refreshToken = saveRefreshToken(optionalUser.get());
            return new AuthResponse(accessToken, refreshToken.getRefreshToken());
        }catch (Exception e){
            throw new BaseException(new ErrorMessage(ErrorMessageType.NO_RECORD_EXIST, e.getMessage()));
        }
    }

    @Override
    public DtoUser register(AuthRequest request) {
        Optional<User> byUsername = userRepository.findByUsername(request.getUsername());
        if (byUsername.isPresent()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.USERNAME_EXIST, "Username already exist"));
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(new Date());
        User savedUser = userRepository.save(user);
        DtoUser dtoUser = new DtoUser();
        BeanUtils.copyProperties(savedUser, dtoUser);
        return dtoUser;
    }


    @Override
    public RefreshToken saveRefreshToken(User user) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserId(user.getId());
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            refreshTokenRepository.delete(refreshToken);
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(new Date());
        refreshToken.setUser(user);
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (optionalRefreshToken.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.TOKEN_ERROR, "Refresh token not found"));
        }

        RefreshToken refreshToken = optionalRefreshToken.get();

        if (!isRefreshTokenExpired(refreshToken.getExpiredDate())) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.TOKEN_ERROR, "Refresh token expired" + refreshToken.getRefreshToken()));
        }

        String accessToken = jwtService.generateJwtToken(refreshToken.getUser());

        RefreshToken newRefreshToken = saveRefreshToken(refreshToken.getUser());

        return new AuthResponse(accessToken, newRefreshToken.getRefreshToken());
    }

    public Boolean isRefreshTokenExpired(Date expireDate) {
        return new Date().before(expireDate);
    }
}
