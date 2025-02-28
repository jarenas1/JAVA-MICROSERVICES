package com.juan.springcloud_msvcOauth.msvc_oauth.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Role {

    private Long id;

    private String name;
}
