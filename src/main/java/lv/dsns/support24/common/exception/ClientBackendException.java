package lv.dsns.support24.common.exception;

import org.springframework.util.Assert;

public class ClientBackendException extends RuntimeException{
    private final ErrorCode errorCode;

    public ClientBackendException(ErrorCode errorCode){
        super();
        Assert.notNull(errorCode, "Error code required");
        this.errorCode = errorCode;
    }

    public ClientBackendException(ErrorCode errorCode, String message){
        super(message);
        Assert.notNull(errorCode, "Error code required");
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
