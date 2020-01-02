package ru.vadimmazitov.voter.util.Exception;

public class ErrorInfo {
    private final String details;

    private final String reqURL;

    private final String rootCause;


    public ErrorInfo(String details, String reqURL, String rootCause) {
        this.details = details;
        this.reqURL = reqURL;
        this.rootCause = rootCause;
    }
}
