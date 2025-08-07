package com.example.api.Other;

import com.example.api.Service.BoothLogService;
import com.example.api.Service.ExpoLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private final ExpoLogService expoLogService;
    private final BoothLogService boothLogService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String sessionID = request.getHeader("Session-ID");

        if(sessionID != null && !sessionID.isEmpty()){
            boolean inExpo = expoLogService.existExpoLogBySessionID(sessionID) &&
                    expoLogService.isOnlineExpoLogBySessionID(sessionID);

            boolean inBooth = boothLogService.existBoothLogBySessionID(sessionID) &&
                    boothLogService.isOnlineBoothLogBySessionID(sessionID);

            if(inExpo){
                expoLogService.updateExpoLogLastActivity(sessionID);
            }
            if(inBooth){
                boothLogService.updateBoothLogLastActivity(sessionID);
            }
        }

        return true;
    }
}