package com.saihoz.e_commerce_api.exception.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String message) {
}
