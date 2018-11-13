package app.display.poi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface POIRepository extends JpaRepository<POI, Long> {

	Optional<POI> findFirstByName(String name);
}
