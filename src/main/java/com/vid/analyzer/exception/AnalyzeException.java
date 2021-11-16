package com.vid.analyzer.exception;

import org.springframework.http.HttpStatus;

public class AnalyzeException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3102633734217156979L;
	private final HttpStatus status;
    public AnalyzeException(HttpStatus status) {
        super();
        this.status = status;
    }
    public AnalyzeException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
    public AnalyzeException(String message, HttpStatus status) {
        super(message);
        this.status =status;
    }
    public AnalyzeException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return this.status;
    }
}
