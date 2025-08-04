package UberBackend.UberBookingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import UberBackend.UberProject_EntityService.models.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}
