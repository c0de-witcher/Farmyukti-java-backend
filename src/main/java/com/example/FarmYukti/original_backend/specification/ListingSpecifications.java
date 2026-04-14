package com.example.FarmYukti.original_backend.specification;

import com.example.FarmYukti.original_backend.model.Listing;
import com.example.FarmYukti.original_backend.model.enums.ListingStatus;
import com.example.FarmYukti.original_backend.responseDTO.ListingSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ListingSpecifications {

    public static Specification<Listing> withDynamicQuery(ListingSearchRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Rule 1: We NEVER show SOLD or CANCELLED listings in search results
            predicates.add(criteriaBuilder.equal(root.get("status"), ListingStatus.ACTIVE));

            if (filter.cropId() != null) {
                // Notice how we traverse the relationship: root.get("crop").get("id")
                predicates.add(criteriaBuilder.equal(root.get("crop").get("id"), filter.cropId()));
            }

            if (filter.minPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("askPricePerKg"), filter.minPrice()));
            }

            if (filter.maxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("askPricePerKg"), filter.maxPrice()));
            }



            // Combine all predicates with an AND operator
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
