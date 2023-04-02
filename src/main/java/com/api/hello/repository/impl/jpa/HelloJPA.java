package com.api.hello.repository.impl.jpa;

import com.api.hello.dto.Hello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface HelloJPA extends JpaRepository<Hello,Long> {
    Hello findByName(String lastName);

}
