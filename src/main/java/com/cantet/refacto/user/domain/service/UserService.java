package com.cantet.refacto.user.domain.service;

import com.cantet.refacto.user.domain.UserRepository;
import com.cantet.refacto.user.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String name, String email) throws InvalidFieldException {
        User user = new User(null, name, email, new Date(), new Date());

        userRepository.addUser(user);
    }

    public void updateUser(String userId, String name, String email) throws InvalidFieldException {
        final User originalSavedUser = userRepository.getUserById(userId);

        final User user = new User(userId, name, email, originalSavedUser.getCreated(), new Date());

        userRepository.updateUser(user);
    }
}
