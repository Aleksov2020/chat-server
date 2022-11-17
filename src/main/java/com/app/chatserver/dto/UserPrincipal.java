package com.app.chatserver.dto;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private String loginName;
    public UserPrincipal(String loginName){
        this.loginName = loginName;
    }
    @Override
    public String getName() {
        return loginName;
    }
}
