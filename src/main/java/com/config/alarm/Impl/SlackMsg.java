package com.config.alarm.Impl;

import com.config.alarm.AlarmMsg;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * fileName       : SlackMsg
 * author         : crlee
 * date           : 2023/04/02
 * description    : Slack 웹훅 메세지 보내기 ( 참고 - https://chung10.tistory.com/159 )
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/02        crlee       최초 생성
 */
@Component
public class SlackMsg implements AlarmMsg {

    private String URL = ".";
    @Override
    public boolean sendMsg(String to, String name) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 파라미터
        Map<String,Object> requestBody = new HashMap<String,Object>();
        requestBody.put("channel" , "#"+to);
        requestBody.put("username" , "apiSvr Alarm");
        requestBody.put("text" , name);
        requestBody.put("icon_emoji" , ":kr:");

        // HTTP 요청 본문 생성
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(this.URL, request, String.class);

        // 응답 본문 가져오기
        String responseBody = response.getBody();
        if( response.getStatusCode() == HttpStatus.OK && responseBody.equals("ok") ){
            return true;
        }
        return false;
    }
}
