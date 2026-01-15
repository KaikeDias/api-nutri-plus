package com.dias.nutri_plus.repositories.specifications;

import com.dias.nutri_plus.dtos.patient.PatientFilterDTO;
import com.dias.nutri_plus.entities.Patient;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;


public class PatientSpecification {

    private PatientSpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Patient> filter(PatientFilterDTO patientFilterDTO) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if(StringUtils.hasText(patientFilterDTO.getName())) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + patientFilterDTO.getName().toLowerCase() + "%"
                        )
                );
            }

            if (StringUtils.hasText(patientFilterDTO.getCpf())) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                root.get("cpf"),
                                "%" + patientFilterDTO.getCpf() + "%"
                        )
                );
            }

            return predicate;
        };
    }
}
