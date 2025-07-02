package jayho.userserver.repository;


import jayho.userserver.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private static final Map<Long, User> repository = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);


    @Override
    public User save(User user) {
        User userCandidate = user;
        if (user.getUserId() == null) {
            userCandidate = User.create(sequence.incrementAndGet(),
                    user.getUserName(),
                    user.getUserProfileImage());
        }
        repository.put(userCandidate.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        // store에서 id로 조회하고, 결과가 null일 수 있으므로 Optional로 감싸서 반환
        return Optional.ofNullable(repository.get(userId));
    }

    @Override
    public List<User> findAll() {
        // store의 모든 value(User)를 리스트로 변환하여 반환
        return new ArrayList<>(repository.values());
    }

    @Override
    public void deleteById(Long id) {
        repository.remove(id);
    }
}
