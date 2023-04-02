package com.config;

import com.config.alarm.AlarmMsg;
import com.config.alarm.Impl.SlackMsg;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SlackMsgTest {

    @Test
    void sendMsgTest(){
        AlarmMsg alarmMsg = new SlackMsg();
        boolean result = alarmMsg.sendMsg("hellow_socket","3번 벨트 불량 발생! - 관리실 방문 요망 - ");
        assertThat( result ).isEqualTo( true );
    }
}
