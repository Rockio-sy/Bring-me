package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLogin(@NotBlank(message = "Email or phone is required filed") String emailOrPhone,@NotBlank(message = "Password needed") String password) {
}
