package org.autotaker.gpt_gen.meeting.reservations.repository;

import java.util.Optional;

import org.autotaker.gpt_gen.meeting.reservations.entity.ReservationManager;
import org.springframework.data.repository.CrudRepository;

public interface ReservationManagerRepository extends CrudRepository<ReservationManager, Long> {

    Optional<ReservationManager> findByEmail(String email);
}
