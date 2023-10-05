package com.devlop.siren.domain.user.dto;

import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailsDto implements org.springframework.security.core.userdetails.UserDetails {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private UserRole userRole;
    private boolean isDeleted;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    @Builder
    public UserDetailsDto(Long id, String email, String password, String nickName, UserRole userRole,
                          boolean isDeleted, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.userRole = userRole;
        this.isDeleted = isDeleted;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static UserDetailsDto fromEntity(User user){
        return UserDetailsDto.builder()
                .id(user.getId())
                .password(user.getPassword())
                .email(user.getEmail())
                .userRole(user.getRole())
                .createdDateTime(user.getCreatedDateTIme())
                .modifiedDateTime(user.getModifiedDateTime())
                .isDeleted(user.isDeleted())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isDeleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isDeleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isDeleted;
    }

    @Override
    public boolean isEnabled() {
        return !this.isDeleted;
    }
}
