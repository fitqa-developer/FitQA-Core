package com.cocovo.fitqaspringjava.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
public class UserInfo {

    @Getter
    @Builder
    @ToString
    public static class Main implements Serializable{
        private String email;
        private String name;
        private String photoURL;
        private User.AccountProvider provider;
    }

}
