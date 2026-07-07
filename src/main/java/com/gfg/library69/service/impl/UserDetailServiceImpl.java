package com.gfg.library69.service.impl;

import com.gfg.library69.domain.User;
import com.gfg.library69.exception.UserAlreadyExistsException;
import com.gfg.library69.repository.UserRepository;
import com.gfg.library69.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserService {
    public UserDetailServiceImpl() {

    }

    public UserDetailServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create the userRepository
     * Autowire here
     * fetch user data from the repository.
     *
     */

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        //return optionalUser.orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    @Override
    public void addUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            user.setAuthority("USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new UserAlreadyExistsException("User already exists");
        }

    }
}



interface NotificationSender {
    void send(String message);
}

@Component
@Primary
class EmailNotificationSender implements NotificationSender {
    public void send(String message) { /* ... */ }
}

@Component
@Qualifier("sms")
 class SmsNotificationSender implements NotificationSender {
    public void send(String message) { /* ... */ }
}

@Service
 class AlertService {

    private final NotificationSender defaultSender; // resolves EmailNotificationSender via
    private final NotificationSender smsSender;

    public AlertService(NotificationSender defaultSender,
                        @Qualifier("sms") NotificationSender smsSender) {
        this.defaultSender = defaultSender;
        this.smsSender = smsSender;
    }
}

// Injecting ALL implementations, e.g. for a strategy/chain pattern
@Service
class BroadcastService {
    private final List<NotificationSender> senders;

    public BroadcastService(List<NotificationSender> senders) {
        this.senders = senders; // Spring injects every matching bean, in defined order
    }

    public void broadcastAll(String msg) {
        senders.forEach(s -> s.send(msg));
    }
}
