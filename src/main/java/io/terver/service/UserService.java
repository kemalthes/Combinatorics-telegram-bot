package io.terver.service;

import io.terver.enums.State;
import io.terver.entity.Mode;
import io.terver.entity.User;
import io.terver.repository.ModeRepository;
import io.terver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModeRepository modeRepository;

    public UserService(UserRepository userRepository, ModeRepository modeRepository) {
        this.userRepository = userRepository;
        this.modeRepository = modeRepository;
    }

    public void updateUser(String chatId, Mode mode) {
        Optional<User> userWrapper = userRepository.findById(Long.valueOf(chatId));
        User user;
        if (userWrapper.isEmpty()) {
            user = new User();
            user.setId(Long.valueOf(chatId));
        } else {
            user = userWrapper.get();
        }
        user.setMode(mode);
        userRepository.save(user);
    }

    public User checkUser(String chatId) {
        Optional<User> userWrapper = userRepository.findById(Long.valueOf(chatId));
        User user;
        if (userWrapper.isEmpty()) {
            user = new User();
            user.setId(Long.valueOf(chatId));
            user.setMode(modeRepository.findByState(State.NONE));
            userRepository.save(user);
        } else {
            user = userWrapper.get();
        }
        return user;
    }

    public void setStateNone(String chatId) {
        User user = userRepository.findById(Long.valueOf(chatId))
                .orElse(new User(Long.valueOf(chatId), modeRepository.findByState(State.NONE)));
        user.setMode(modeRepository.findByState(State.NONE));
        userRepository.save(user);
    }
}