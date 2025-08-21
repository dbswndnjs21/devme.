package com.erp.service;

import com.erp.dto.HomeUserInfo;
import com.erp.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomUserDetails implements UserDetails {

    @Getter
    private User user;
    private Map<String, Object> attributes;  // OAuth2User의 속성

    // 기본 생성자
    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    public Long getId() {
    	return user.getId();
    }
    
    public String getRole(){
        return user.getRole().toString();
    }

    public String getPosition() {
        return user.getPosition();
    }

    public String getDepartment() {
        return user.getDepartment();
    }

    public HomeUserInfo toHomeUserInfo() {
        return new HomeUserInfo(
                user.getId(),
                user.getUsername(),
                user.getPosition(),
                user.getDepartment(),
                user.getAddress(),
                user.getLatitude(),
                user.getLongitude()
        );
    }
}
