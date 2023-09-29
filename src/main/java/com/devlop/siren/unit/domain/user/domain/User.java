package com.devlop.siren.unit.domain.user.domain;

import com.devlop.siren.unit.domain.user.util.AllergyConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.EnumSet;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CUSTOMER;

    @Column(name = "allergy")
    @Convert(converter = AllergyConverter.class)
    private EnumSet<AllergyType> allergies;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    private User(String email, String password, String nickName, String phone, UserRole role, EnumSet<AllergyType> allergies, boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.role = role;
        this.allergies = allergies;
        this.isDeleted = false;
    }

    public void delete(){
        this.isDeleted = true;
    }
    public void changeRole(UserRole role){
        this.role = role;
    }
}
