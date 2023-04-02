package com.api.hello.service.Impl;

import com.api.hello.service.HelloService;
import com.config.alarm.AlarmMsg;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class HelloDecorator implements HelloService {
    private final HelloService helloService;
    private final AlarmMsg alarmMsg;
    public HelloDecorator(HelloService helloService, AlarmMsg alarmMsg) {
        this.helloService = helloService;
        this.alarmMsg = alarmMsg;
    }

    @Override
    public String sayHello(String name) {
        alarmMsg.sendMsg("hellow_socket","유저가 입장했습니다 : Hello "+name);
        return "*" + helloService.sayHello(name) + "*";
    }

    @Override
    public int countOf(String name) {
        return helloService.countOf(name);
    }
}
