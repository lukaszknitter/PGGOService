package app.models.building;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

	Optional<Building> findFirstByName(String name);
}
