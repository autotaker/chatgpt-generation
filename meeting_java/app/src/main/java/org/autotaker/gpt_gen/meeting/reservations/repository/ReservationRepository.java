package org.autotaker.gpt_gen.meeting.reservations.repository;

import org.autotaker.gpt_gen.meeting.reservations.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
