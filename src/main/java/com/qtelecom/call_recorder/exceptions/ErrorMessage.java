package com.qtelecom.call_recorder.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
