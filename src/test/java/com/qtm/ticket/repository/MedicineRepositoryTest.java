package com.qtm.ticket.repository;

import com.qtm.ticket.entity.MedicineEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.parser.PartTree;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Verifica che le query derivate restino compatibili con i campi di MedicineEntity.
 */
class MedicineRepositoryTest {

    @Test
    void derivedQueriesShouldReferenceExistingEntityProperties() {
        List<Method> derivedQueryMethods = Arrays.stream(MedicineRepository.class.getDeclaredMethods())
                .filter(method -> !method.isAnnotationPresent(Query.class))
                .toList();

        assertThat(derivedQueryMethods).isNotEmpty();
        derivedQueryMethods.forEach(method -> assertThatCode(
                () -> new PartTree(method.getName(), MedicineEntity.class))
                .as("Derived query %s", method.getName())
                .doesNotThrowAnyException());
    }
}