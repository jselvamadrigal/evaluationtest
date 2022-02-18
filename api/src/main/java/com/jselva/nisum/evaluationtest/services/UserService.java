package com.jselva.nisum.evaluationtest.services;

import com.jselva.nisum.evaluationtest.data.dto.UserDto;
import com.jselva.nisum.evaluationtest.data.entity.User;

import java.util.Optional;

public interface UserService extends BaseService<User, UserDto> {
    Optional<String> authenticate(String email, String password);

}
