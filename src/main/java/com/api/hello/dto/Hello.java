package com.api.hello.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="hello")
@ToString
public class Hello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long idx;

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    @Column
    @ColumnDefault("0")
    private int count = 0;

    @Column
    @ColumnDefault("now()")
    @CreationTimestamp
    private LocalDate ins_timestamp;

    @Column
    @UpdateTimestamp
    private LocalDate upd_timestamp;

}
