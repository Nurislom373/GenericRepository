package org.khasanof;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Hello {
    private String name;
    private String fromh;
    private BigDecimal lock;
    private Timestamp now;
    private long amount;
}
