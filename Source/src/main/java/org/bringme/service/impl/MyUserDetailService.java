package org.bringme.service.impl;

import org.bringme.exceptions.CustomException;
import org.bringme.exceptions.LockedAccountException;
import org.bringme.exceptions.NotFoundException;
import org.bringme.model.AuthUserDetails;
import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final AuthRepository authRepository;

    /**
     * Constructor to initialize the {@link MyUserDetailService} with the given {@link AuthRepository}.
     * The {@link AuthRepository} will be used to fetch user data based on the email or phone provided during authentication.
     *
     * @param authRepository The {@link AuthRepository} instance for accessing user data.
     */
    public MyUserDetailService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Loads user details by username (email or phone).
     * Checks if the user exists and if their account is locked. If the user is found and the account is not locked,
     * it returns the user details.
     *
     * @param emailOrPhone The email or phone of the user to authenticate.
     * @return {@link UserDetails} The user details, wrapped in an {@link AuthUserDetails} object.
     * @throws UsernameNotFoundException If the user cannot be found in the repository.
     * @throws CustomException           If the account is locked or an error occurs while fetching the user.
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException, CustomException {
        Optional<Person> user = authRepository.getByEmailOrPhone(emailOrPhone);

        if (user.isEmpty()) {
            throw new NotFoundException("User not found", "LoadUserByUsername");
        }

        if (user.get().getAccountStatus() > 1) {

            throw new LockedAccountException(emailOrPhone);
        }

        return new AuthUserDetails(user.get());
    }
}
