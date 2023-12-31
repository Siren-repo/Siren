package com.devlop.siren.domain.user.domain;

import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.user.utils.KoreanNickname;
import com.devlop.siren.global.common.BaseEntity;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(unique = true)
  @Email(message = "이메일 형식에 맞지 않습니다")
  private String email;

  @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
  private String password;

  @KoreanNickname
  @Size(max = 6, message = "닉네임은 한글로 6자까지 가능합니다")
  private String nickName;

  @Pattern(regexp = "^01(?:0|1|[6-9])-(\\d{3,4})-(\\d{4})$", message = "핸드폰번호 형식에 맞지 않습니다")
  private String phone;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "allergy")
  @Convert(converter = AllergyConverter.class)
  private EnumSet<AllergyType> allergies;

  @OneToMany(mappedBy = "user")
  private List<Order> orders = new ArrayList<Order>();

  @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
  private boolean isDeleted;

  @Builder
  private User(
      String email,
      String password,
      String nickName,
      String phone,
      UserRole role,
      EnumSet<AllergyType> allergies) {
    this.email = Objects.requireNonNull(email);
    this.password = Objects.requireNonNull(password);
    this.nickName = Objects.requireNonNull(nickName);
    this.phone = Objects.requireNonNull(phone);
    this.role = role;
    this.allergies = allergies;
    this.isDeleted = false;
  }

  public void delete() {
    this.isDeleted = true;
  }

  public void changeRole(UserRole role) {
    this.role = role;
  }
}
