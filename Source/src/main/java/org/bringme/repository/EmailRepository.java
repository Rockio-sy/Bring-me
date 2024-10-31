package org.bringme.repository;

public interface EmailRepository {
    void saveCode(String email, String code);

    String getCode(String emailOrPhone);
}
