package org.autotaker.gpt_gen.meeting.reservations;

import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
