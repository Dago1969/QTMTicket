package com.qtm.ticket.service;

import com.qtm.ticket.dto.StructureDepartmentDto;
import com.qtm.ticket.entity.DisciplinaEntity;
import com.qtm.ticket.entity.HospitalEntity;
import com.qtm.ticket.entity.StructureDepartmentEntity;
import com.qtm.ticket.mapper.StructureDepartmentMapper;
import com.qtm.ticket.repository.DisciplinaRepository;
import com.qtm.ticket.repository.HospitalRepository;
import com.qtm.ticket.repository.StructureDepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StructureDepartmentServiceTest {

    @Mock
    private StructureDepartmentRepository structureDepartmentRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @Test
    void findByStructureShouldReturnDepartmentsUsingHospitalRelationship() {
        StructureDepartmentService structureDepartmentService = new StructureDepartmentService(
            structureDepartmentRepository,
            new StructureDepartmentMapper(),
            hospitalRepository,
            disciplinaRepository
        );

        StructureDepartmentEntity entity = StructureDepartmentEntity.builder()
                .id(1L)
                .structure(HospitalEntity.builder().id(10L).codiceStruttura("090623").struttura("Ospedale S.Andrea").build())
                .disciplina(DisciplinaEntity.builder().codiceDisciplina("08").disciplina("Cardiologia").build())
                .indirizzo("Via Risorgimento, 43")
                .build();

        when(structureDepartmentRepository.findByStructure_CodiceStruttura("090623")).thenReturn(List.of(entity));

        List<StructureDepartmentDto> result = structureDepartmentService.findByStructure("090623");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodiceStruttura()).isEqualTo("090623");
        assertThat(result.get(0).getCodiceDisciplina()).isEqualTo("08");
        assertThat(result.get(0).getDisciplina()).isEqualTo("Cardiologia");
        assertThat(result.get(0).getIndirizzo()).isEqualTo("Via Risorgimento, 43");
    }

    @Test
    void saveShouldResolveHospitalEntityByCodiceStruttura() {
        StructureDepartmentService structureDepartmentService = new StructureDepartmentService(
            structureDepartmentRepository,
            new StructureDepartmentMapper(),
            hospitalRepository,
            disciplinaRepository
        );

        StructureDepartmentDto dto = StructureDepartmentDto.builder()
                .codiceStruttura("090623")
                .codiceDisciplina("08")
                .indirizzo("Via Risorgimento, 43")
                .build();

        HospitalEntity hospital = HospitalEntity.builder().id(10L).codiceStruttura("090623").build();
        DisciplinaEntity disciplina = DisciplinaEntity.builder().codiceDisciplina("08").disciplina("Cardiologia").build();

        when(hospitalRepository.findTopByCodiceStrutturaOrderByIdAsc("090623")).thenReturn(Optional.of(hospital));
        when(disciplinaRepository.findById("08")).thenReturn(Optional.of(disciplina));
        when(structureDepartmentRepository.save(any(StructureDepartmentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StructureDepartmentDto saved = structureDepartmentService.save(dto);

        ArgumentCaptor<StructureDepartmentEntity> captor = ArgumentCaptor.forClass(StructureDepartmentEntity.class);
        verify(structureDepartmentRepository).save(captor.capture());
        assertThat(captor.getValue().getStructure()).isSameAs(hospital);
        assertThat(captor.getValue().getDisciplina()).isSameAs(disciplina);
        assertThat(saved.getCodiceStruttura()).isEqualTo("090623");
        assertThat(saved.getCodiceDisciplina()).isEqualTo("08");
        assertThat(saved.getDisciplina()).isEqualTo("Cardiologia");
    }
}