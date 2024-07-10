package lv.dsns.support24.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler {
    @Getter
    private Map<String, ErrorCode> errorCodeMapper = new HashMap<>();

    private ResponseEntity<Object> doHandleException(ErrorCode errorCode, Exception e) {
        logException(e);

        HttpStatus httpStatus;
        try {
            httpStatus = HttpStatus.valueOf(errorCode.getData().getHttpResponseCode());
        } catch (Exception exception) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode = ErrorCode.UNKNOWN_SERVER_ERROR;
        }

        ErrorCode.Data data = errorCode.getData();
        if (Objects.nonNull(e.getMessage())) {
            data.setDescription(String.format("%s : %s", data.getDescription(), e.getMessage()));
        }

        data.setLabel(errorCode.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String errorCodeJson = String.format("{ \"errorCode\": %s}", errorCode.getData().getCode());
        try {
            errorCodeJson = objectMapper.writeValueAsString(errorCode.getData());
        } catch (IOException i) {
            // pass
        }

        return ResponseEntity
                .status(httpStatus)
                .body(errorCodeJson);
    }

    @ExceptionHandler(ClientBackendException.class)
    public ResponseEntity<Object> handleException(ClientBackendException e) {
        ErrorCode errorCode = e.getErrorCode();
        return doHandleException(errorCode, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponce handleNotFoundException(HttpRequestMethodNotSupportedException exception) {
        return new ErrorResponce(
                System.currentTimeMillis(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                getErrors(exception)
        );
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleException(InvalidFormatException e) {
        return doHandleException(ErrorCode.ILLEGAL_PARAM_TYPE, e);
    }

//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<Object> handleException(ExpiredJwtException e) {
//        return doHandleException(ErrorCode.JWT_TOKEN_EXPIRED, e);
//    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.ILLEGAL_PARAM_TYPE;
        return doHandleException(errorCode, e);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleException(DateTimeParseException e) {
        ErrorCode errorCode = ErrorCode.ILLEGAL_PARAM_TYPE;
        return doHandleException(errorCode, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Optional<String> matchingException =
                getErrorCodeMapper().keySet().stream().filter(m -> e.getMessage().contains(m)).findAny();
        if (matchingException.isEmpty()) {
            log.error("matching exception is empty");
            return doHandleException(ErrorCode.UNKNOWN_SERVER_ERROR, e);
        } else {
            ErrorCode errorCode = getErrorCodeMapper().get(matchingException.get());
            return doHandleException(errorCode, e);
        }
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponce handle(BindException exception) {

        return new ErrorResponce(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .map(x -> FormValidationError.builder()
                                .field(x.getField())
                                .message(x.getDefaultMessage())
                                .build()).collect(Collectors.toList())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponce handle(MethodArgumentNotValidException exception) {

        return new ErrorResponce(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .map(x -> FormValidationError.builder()
                                .field(x.getField())
                                .message(x.getDefaultMessage())
                                .build()).collect(Collectors.toList())
        );
    }

    @NotNull
    private List<Error> getErrors(Exception exception) {
        return List.of(Error.builder()
                .message(exception.getMessage())
                .build());
    }

    private void logException(Exception e) {
        log.error("Handling exception ", e);
    }

}