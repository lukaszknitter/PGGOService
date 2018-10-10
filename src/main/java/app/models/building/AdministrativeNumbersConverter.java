package app.models.building;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class AdministrativeNumbersConverter implements AttributeConverter<List<Integer>, String> {

	@Override
	public String convertToDatabaseColumn(List<Integer> administrativeNumbers) {
		if (administrativeNumbers == null || administrativeNumbers.isEmpty()) {
			return null;
		}
		return administrativeNumbers.stream()
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.collect(Collectors.joining(","));
	}

	@Override
	public List<Integer> convertToEntityAttribute(String s) {
		if (s == null || s.isEmpty()) {
			return new ArrayList<>();
		}
		return Stream.of(s.split(","))
				.map(Integer::parseInt)
				.collect(Collectors.toList());
	}
}
