package com.lagnashree.customermanagement.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Builder
@Data
public class CustomErrorResponse {

    String description;
    String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/mm/yyyy hh:mm:ss")
    LocalDateTime timestamp;

}