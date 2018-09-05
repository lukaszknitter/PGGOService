package app.display.building;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingDisplayRepository extends JpaRepository<BuildingDisplay, Long> {

	Optional<BuildingDisplay> findFirstByName(String name);
}
