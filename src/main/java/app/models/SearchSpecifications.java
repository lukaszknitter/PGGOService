package app.models;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SearchSpecifications {

	public Specification<BaseEntity> nameContains(String name) {
		return (root, query, builder) -> builder.like(root.<String>get("name"), "%" + name + "%");
	}
}
