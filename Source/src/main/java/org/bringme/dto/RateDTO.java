package org.bringme.dto;

import jakarta.validation.constraints.*;

public record RateDTO(Long id,
                      @Positive(message = "No such user") int ratedUserId,
                      String comment,
                      @Min(value = 1, message = "Minimum value is 1")
                      @Max(value = 5, message = "Maximum value is 5")
                      @NotNull(message = "Value cannot be null")
                      int value,

                      @Positive(message = "No such request") int requestId) {
}
