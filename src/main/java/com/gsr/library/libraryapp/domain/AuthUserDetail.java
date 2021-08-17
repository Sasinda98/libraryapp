package com.gsr.library.libraryapp.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AuthUserDetail extends User implements UserDetails {

    public AuthUserDetail(User u) {
        super(u);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ArrayList<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        super.getRoles().forEach(role -> {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getName()));

            role.getPermissions().forEach(permission -> {
                grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getName()));
            });

        });
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

}
