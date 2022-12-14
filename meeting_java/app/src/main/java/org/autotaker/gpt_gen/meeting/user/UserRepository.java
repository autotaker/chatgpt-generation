package org.autotaker.gpt_gen.meeting.user;

import java.util.Optional;

import org.autotaker.gpt_gen.meeting.user.entity.User;
import org.autotaker.gpt_gen.meeting.user.value.UserEmail;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(UserEmail email);

    Optional<User> findByEmail(UserEmail email);

}
