package com.snut_likelion.global.auth.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class SnutLikeLionUser implements UserDetails {

    private static final String ROLE_GUEST = "ROLE_GUEST";

    private UserInfo userInfo;

    private SnutLikeLionUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static SnutLikeLionUser from(UserInfo userInfo) {
        return new SnutLikeLionUser(userInfo);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
                userInfo.getRole() == null ? ROLE_GUEST : userInfo.getRole());
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userInfo.getEmail();
    }

    public Long getId() {
        return this.userInfo.getId();
    }

    public String getEmail() {
        return this.userInfo.getEmail();
    }

}
