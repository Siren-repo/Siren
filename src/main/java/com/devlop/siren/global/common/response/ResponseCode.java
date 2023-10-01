package com.devlop.siren.global.common.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ResponseCode {

    public enum Error {

        DUPLICATE_CATEGORY,
        INVALID_ALLERGY_TYPE,
        INVALID_CATEGORY_TYPE,
        NOT_FOUND,
        NOT_VALID;

    }
    public enum Normal {
        UPDATE,
        CREATE,
        DELETE,
        RETRIEVE;
    }
}
