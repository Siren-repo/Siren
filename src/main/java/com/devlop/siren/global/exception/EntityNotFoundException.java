package com.devlop.siren.global.exception;

import lombok.Getter;


public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {

    public static final String errorMessage = "해당하는 엔티티를 찾을 수 없습니다";

    public EntityNotFoundException() {
        super(errorMessage);
    }

}
