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
    INVALID_CURRENT_PASSWORD(
            "400-007",
            "Invalid current passwrod",
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
            404
    ),
    PROBLEM_NOT_FOUND(
            "404-005",
            "Problem not found",
            404
    ),
    UNIT_NOT_FOUND(
            "404-006",
            "Unit not found",
            404
    ),
    USER_NOT_ACTIVE(
            "404-007",
            "User not active",
            404
    ),
    INVENTORY_NUMBER_ALREADY_EXISTS(
            "404-008",
            "Inventory number already exists",
            404
    ),
    DEVICE_NOT_FOUND(
            "404-009",
            "Device not found",
            404
    ),
    GROUP_ALREADY_EXISTS(
            "404-010",
            "Nabat group for such unit already exists",
            404
    ),
    NABAT_GROUP_NOT_FOUND(
            "404-011",
            "Nabat group not found",
            404
    ),
    INVALID_UNIT(
            "404-012",
            "Provided unit not in list of child units",
            404
    ),
    GROUP_IS_EMPTY(
            "404-013",
            "No users provided in nabat group",
            404
    ),
    NO_PHONES_FOUND(
            "404-015",
            "No phones found in nabat group",
            404
    ),
    EVENT_NOT_FOUND(
            "404-016",
            "Event not found",
            404
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
            500),

    NOTIFICATION_FAILED(
            "501",
            "Notification failed",
            501
    ),
    GET_NOTIFICATION_RESULT_FAILED(
            "502",
            "Get notification result failed",
            501
    ),;



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
