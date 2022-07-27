package org.khasanof;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthUser {
    private Integer id;
    private String name;
    private String password;
}
