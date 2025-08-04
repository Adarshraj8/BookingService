package UberBackend.UberBookingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import UberBackend.UberProject_EntityService.models.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>{

	
}
