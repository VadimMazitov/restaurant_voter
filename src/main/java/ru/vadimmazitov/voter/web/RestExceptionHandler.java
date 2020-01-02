package ru.vadimmazitov.voter.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vadimmazitov.voter.util.Exception.ErrorInfo;
import ru.vadimmazitov.voter.util.Exception.NotFoundException;
import ru.vadimmazitov.voter.util.ValidationUtil;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    private static final int HTTP_UNKNOWN_ERROR = 520;

    @ExceptionHandler({NoResultException.class, NotFoundException.class})
    public ResponseEntity<ErrorInfo> noResult(HttpServletRequest req, Exception e) {
        ErrorInfo errorInfo = getErrorInfo(e, req, "Requested entity does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInfo);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorInfo> unknownError(HttpServletRequest req, Exception e) {
        ErrorInfo errorInfo = getErrorInfo(e, req, "Unknown error occurred");
        return ResponseEntity.status(HTTP_UNKNOWN_ERROR).body(errorInfo);
    }

    private ErrorInfo getErrorInfo(Throwable t, HttpServletRequest req, String details) {
        String rootCauseMessage = ValidationUtil.getRootCause(t).getMessage();
        String reqURL = req.getRequestURL().toString();
        return new ErrorInfo(details, reqURL, rootCauseMessage);
    }

}
