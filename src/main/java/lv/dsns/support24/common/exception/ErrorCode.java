package lv.dsns.support24.common.exception;

import lombok.Getter;
import lombok.Setter;

public enum ErrorCode {
    //400
    ILLEGAL_PARAM_TYPE("400-001",
            "Illegal parameter type",
            400),
    BAD_CREDENTIALS(
            "400-002",
            "Bad credentials",
            400
    ),
    USER_ALREADY_EXIST(
            "400-003",
            "User already exist",
            400
    ),
    JWT_TOKEN_EXPIRED(
            "400-004",
            "JWT is expired",
            400
    ),
    JWT_TOKEN_INVALID(
            "400-005",
            "JWT is invalid",
            400
    ),
    REFRESH_TOKEN_EXPIRED(
            "400-006",
            "REFRESH_TOKEN_EXPIRED",
            400
    ),

    // 404
    NOT_FOUND(
            "404-001",
            "Not found",
            404
    ),
    USER_NOT_FOUND(
            "404-002",
            "User not found",
            404
    ),
    TASK_NOT_FOUND(
            "404-003",
            "Task not found",
            404
    ),
    USER_ALREADY_EXISTS(
            "404-004",
            "User already exists",
            405
    ),
    //403
    FORBIDDEN(
            "403",
            "Forbidden",
            403
    ),
    //500
    UNKNOWN_SERVER_ERROR("500",
            "Unknown server error",
            500);


    private Data data;

    ErrorCode(String code, String description, int httpResponseCode) {
        this.data = new Data(code, description, httpResponseCode);
    }

    public Data getData() {
        return data;
    }

    public final class Data {
        @Getter
        private String code;

        @Getter
        @Setter
        private String description;
        @Getter
        private int httpResponseCode;
        @Getter
        @Setter
        private String label;

        public Data(String code, String description, int httpResponseCode) {
            this.code = code;
            this.description = description;
            this.httpResponseCode = httpResponseCode;
        }
    }


}
