package com.moudjames23.marksms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {

    private int code;
    private String message;
    private Map<String, ?> data;
    private Map<String, ?> errors;


}
