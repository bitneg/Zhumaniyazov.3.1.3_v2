package zhumaniyazov.boot.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zhumaniyazov.boot.model.User;
import zhumaniyazov.boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService  {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}

@Override
public List<User> getAllUsers() {
    logger.info("Получение списка всех пользователей");
    return userRepository.findAll();
}

@Transactional
@Override
public void updateUser(Long id, User user) {
    logger.info("Обновление пользователя с ID {}", id);
    Optional<User> existingUserOptional = userRepository.findById(id);

    if (existingUserOptional.isPresent()) {
        User existingUser = existingUserOptional.get();
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setAge(user.getAge());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        existingUser.setRoles(user.getRoles());
        userRepository.save(existingUser);
        logger.info("Пользователь с ID {} успешно обновлен", id);
    } else {
        logger.warn("Попытка обновления несуществующего пользователя с ID {}", id);
    }
}

@Transactional
@Override
public void deleteUser(Long id) {
    logger.info("Удаление пользователя с ID {}", id);
    if (!userRepository.existsById(id)) {
        logger.error("Пользователь не обнаружен, его ID: {}", id);
        throw new EntityNotFoundException("пользователь не обнаружен его id: " + id);
    }
    userRepository.deleteById(id);
    logger.info("Пользователь с ID {} успешно удален", id);
}

@Override
public User getUserById(Long id) {
    logger.info("Получение пользователя с ID {}", id);
    return userRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Пользователь не обнаружен, его ID: {}", id);
                return new EntityNotFoundException("пользователь не обнаружен его id: " + id);
            });
}

@Transactional
@Override
public void saveUser(User user) {
    logger.info("Сохранение нового пользователя: {}", user.getUsername());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    logger.info("Пользователь {} успешно сохранен", user.getUsername());
}

}
