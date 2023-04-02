package com.config.alarm.Impl;

import com.config.alarm.AlarmMsg;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SlackMsg implements AlarmMsg {
    @Override
    public boolean sendMsg(String to, String name) {

        String url = "https://hooks.slack.com/services/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";

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
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 응답 본문 가져오기
        String responseBody = response.getBody();
        if( response.getStatusCode() == HttpStatus.OK && responseBody.equals("ok") ){
            return true;
        }
        return false;
    }
}
