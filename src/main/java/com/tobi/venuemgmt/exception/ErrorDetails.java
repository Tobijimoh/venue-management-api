package com.tobi.venuemgmt.exception;

import java.time.LocalDateTime;

/**
 * ErrorDetails represents a standard error response structure
 * for the Venue Management API.
 */
public class ErrorDetails {
    private LocalDateTime timestamp;
    private int status;         // HTTP status code (e.g., 404)
    private String error;       // HTTP status text (e.g., "Not Found")
    private String message;     // Detailed error message
    private String path;        // The request path that caused the error

    public ErrorDetails(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
