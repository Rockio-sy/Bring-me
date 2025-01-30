package org.bringme.dto;

import jakarta.validation.constraints.*;

/**
 *
 * @param id Rate id primary key
 * @param ratedUserId User that has been rated
 * @param comment To write under the rate value
 * @param value Rate value
 * @param requestId Because of it user has been rated
 */
public record RateDTO(Long id,
                      @Positive(message = "No such user") int ratedUserId,
                      String comment,
                      @Min(value = 1, message = "Minimum value is 1")
                      @Max(value = 5, message = "Maximum value is 5")
                      @NotNull(message = "Value cannot be null")
                      int value,

                      @Positive(message = "No such request") int requestId) {
}
