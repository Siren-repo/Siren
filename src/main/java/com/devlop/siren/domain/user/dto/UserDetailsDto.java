package com.devlop.siren.domain.user.dto;

import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailsDto implements UserDetails {
  private Long id;
  private String email;
  private String password;
  private String nickName;
  private String phone;
  private UserRole userRole;
  private EnumSet<AllergyType> allergies;
  private boolean isDeleted;

  @Builder
  public UserDetailsDto(
      Long id,
      String email,
      String password,
      String nickName,
      String phone,
      UserRole userRole,
      EnumSet<AllergyType> allergies,
      boolean isDeleted) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.phone = phone;
    this.userRole = userRole;
    this.allergies = allergies;
    this.isDeleted = isDeleted;
  }

  public static UserDetailsDto fromEntity(User user) {
    return UserDetailsDto.builder()
        .id(user.getId())
        .password(user.getPassword())
        .email(user.getEmail())
        .nickName(user.getNickName())
        .phone(user.getPhone())
        .allergies(user.getAllergies())
        .userRole(user.getRole())
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
