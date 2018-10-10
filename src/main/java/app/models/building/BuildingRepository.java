package app.models.building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long>, JpaSpecificationExecutor {

	Optional<Building> findFirstByName(String name);
}
