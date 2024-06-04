package lv.dsns.support24.common.exception;

import org.springframework.http.HttpStatus;

public class JwtAuthenticationException extends ArithmeticException{
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

}
