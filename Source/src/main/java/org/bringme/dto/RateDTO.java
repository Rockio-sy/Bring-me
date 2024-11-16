package org.bringme.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record RateDTO(Long id,
                      @Positive(message = "No such user") int ratedUserId,
                      String comment,
                      @Min(value = 1) @Max(5) int value,
                      @Positive(message = "No such request") int requestId) {
}
