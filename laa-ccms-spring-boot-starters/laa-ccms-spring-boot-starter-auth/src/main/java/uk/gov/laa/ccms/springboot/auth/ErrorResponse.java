package uk.gov.laa.ccms.springboot.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int code;

    private String status;

    private String message;
}
