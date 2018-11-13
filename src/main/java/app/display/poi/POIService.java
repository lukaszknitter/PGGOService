package app.display.poi;

import app.exception.ConflictException;
import app.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class POIService {

	private final ModelMapper mapper;
	private final POIRepository POIRepository;

	public POIDto createPOI(POIDto dto) {
		Optional<POI> POIWithSameName = POIRepository.findFirstByName(dto.getName());
		if (POIWithSameName.isPresent()) {
			throw new ConflictException(String.format("poi with name '%s' already exists", dto.getName()));
		}

		POI poi = mapper.map(dto, POI.class);
		System.out.println(poi.getName());

		POIRepository.save(poi);
		return mapper.map(poi, POIDto.class);
	}

	POIDto getPOI(long id) {
		POI POI = POIRepository.findById(id).orElseThrow(POINotFoundException(id));
		return mapper.map(POI, POIDto.class);
	}

	ArrayList<POIDto> getPOI() {
		ArrayList<POI> result = new ArrayList<>(POIRepository.findAll());
		Type listType = new TypeToken<List<POIDto>>() {
		}.getType();
		return mapper.map(result, listType);
	}

	POIDto updatePOI(long id, POIDto dto) {
		POI poi = POIRepository.findById(id).orElseThrow((POINotFoundException(id)));
		mapper.map(dto, poi);

		Optional<POI> POIWithSameName = POIRepository.findFirstByName(dto.getName());
		if (POIWithSameName.isPresent() && POIWithSameName.get().getId() != id) {
			throw new ConflictException(String.format("POI with name '%s' already exists", dto.getName()));
		}

		POI saved = POIRepository.saveAndFlush(poi);
		return mapper.map(saved, POIDto.class);
	}

	void deletePOI(long id) {
		POI POI = POIRepository.findById(id).orElseThrow((POINotFoundException(id)));
		POIRepository.delete(POI);
	}

	private Supplier<ResourceNotFoundException> POINotFoundException(long id) {
		return () -> new ResourceNotFoundException(String.format("POI with id %d could not be found", id));
	}
}
