package com.chaticat.authservice.feign.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserRequest {

    private UUID id;
    private String username;
}
