package org.autotaker.gpt_gen.meeting.reservations;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ReservationManagerRepository extends CrudRepository<ReservationManager, Long> {

    Optional<ReservationManager> findByEmail(String email);
}
