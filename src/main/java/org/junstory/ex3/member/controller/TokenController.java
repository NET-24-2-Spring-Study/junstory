package org.junstory.ex3.member.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junstory.ex3.member.dto.MemberDTO;
import org.junstory.ex3.member.security.util.JWTUtil;
import org.junstory.ex3.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/token")
@Log4j2
@RequiredArgsConstructor
public class TokenController {

    private final MemberService memberService;

    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody MemberDTO memberDTO){

        log.info("make token..............");

        MemberDTO memberDTOResult = memberService.read(memberDTO.getMid(), memberDTO.getMpw());

        log.info(memberDTOResult);

        String mid = memberDTOResult.getMid();

        Map<String, Object> dataMap = memberDTOResult.getDataMap();

        String accessToken = jwtUtil.createToken(dataMap, 10);

        String refreshToken = jwtUtil.createToken(Map.of("mid",mid),60*24*7);

        log.info("accessToken: " + accessToken);
        log.info("refreshToken " + refreshToken);

        //return null;
        return ResponseEntity.ok(Map.of("accessToken",accessToken, "refreshToken",refreshToken));

    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @RequestHeader("Authorization")  String accessTokenStr,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("mid") String mid){
    //토큰 존재 확인
        log.info("access token with Bearer: " + accessTokenStr);

        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")){
            return handleException("NO Access Token", 400);
        }
        if(refreshToken == null){
            return handleException("NO Refresh Token", 400);
        }

        log.info("refresh token........... " + refreshToken);

        if (mid==null){
            return handleException("NO MID", 400);
        }

        //Access Token 만료 여부
        String accessToekn = accessTokenStr.substring(7);

        try{
            jwtUtil.validateToken(accessToekn);
            Map<String ,String> data = makeData(mid, accessToekn, refreshToken);
            return ResponseEntity.ok(data);
        }catch (ExpiredJwtException expiredJwtException){
            try{//refresh 필요
                Map<String, String> newTokenMap = makeNewToken(mid, refreshToken);
                return ResponseEntity.ok(newTokenMap);
            }catch (Exception e){
                return handleException("REFRESH "+ e.getMessage(), 400);
            }
        } catch (Exception e){
            return handleException(e.getMessage(), 400);
        }


    }
    private Map<String, String> makeNewToken(String mid, String refreshToken){

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        log.info("refresh token claims: " + refreshToken);
        if(!mid.equals(claims.get("mid").toString())){
            throw new RuntimeException("Invalid Refresh Token Host");
        }

        MemberDTO memberDTO = memberService.getByMid(mid);
        Map<String, Object> dataMap = memberDTO.getDataMap();
        String newAccessToken = jwtUtil.createToken(dataMap, 10);
        String newRefreshToken = jwtUtil.createToken(Map.of("mid",mid),60*24*7);

        return makeData(mid, newAccessToken, newRefreshToken);

    }
    private Map<String, String> makeData(String mid, String accessToken, String refreshToken){
        return Map.of("mid",mid,"accessToken",accessToken,"refreshToken",refreshToken);
    }

    private ResponseEntity<Map<String, String>> handleException(String msg, int status){
        return ResponseEntity.status(status).body(Map.of("error",msg));
    }
}
