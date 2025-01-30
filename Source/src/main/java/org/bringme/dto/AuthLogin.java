package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;

/***
 *
 * @param emailOrPhone Email or phone number used to log in.
 * @param password Password of the user.
 */
public record AuthLogin(@NotBlank(message = "Email or phone is required filed") String emailOrPhone,
                        @NotBlank(message = "Password needed") String password) {
}
