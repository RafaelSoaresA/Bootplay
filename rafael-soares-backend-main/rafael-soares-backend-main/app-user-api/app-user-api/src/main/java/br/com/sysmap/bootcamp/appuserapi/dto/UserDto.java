package br.com.sysmap.bootcamp.appuserapi.dto;

import jakarta.persistence.Column;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String tel;
}
