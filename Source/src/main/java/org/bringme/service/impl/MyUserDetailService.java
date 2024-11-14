package org.bringme.service.impl;

import org.bringme.model.AuthUserDetails;
import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.bringme.service.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final AuthRepository authRepository;

    public MyUserDetailService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException, CustomException{
        Optional<Person> user = authRepository.getByEmailOrPhone(emailOrPhone);

        if (user.isEmpty()) {
            throw new CustomException("User nor found", HttpStatus.UNAUTHORIZED);
        }

        if(user.get().getAccountStatus() > 1){
            throw new CustomException("Account is locked", HttpStatus.LOCKED);
        }

        return new AuthUserDetails(user.get());
    }
}
