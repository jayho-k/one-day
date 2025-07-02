package jayho.userserver.repository;

import jayho.userserver.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(Long userId);
    List<User> findAll();
    void deleteById(Long userId);

}
