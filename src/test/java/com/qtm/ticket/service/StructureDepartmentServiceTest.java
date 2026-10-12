package com.qtm.ticket.service;

import com.qtm.commonlib.dto.StructureDepartmentSourceDto;
import com.qtm.ticket.client.HealthStructureClient;
import com.qtm.ticket.dto.StructureDepartmentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StructureDepartmentServiceTest {

    @Mock
    private HealthStructureClient healthStructureClient;

    @Test
    void findByStructureShouldReturnRemoteDepartments() {
        StructureDepartmentService structureDepartmentService = new StructureDepartmentService(healthStructureClient);
        StructureDepartmentSourceDto remoteDepartment = StructureDepartmentSourceDto.builder()
                .id(1L)
                .codiceStruttura("090623")
                .codiceDisciplina("08")
                .disciplina("Cardiologia")
                .indirizzo("Via Risorgimento, 43")
                .build();

        when(healthStructureClient.findDepartments("090623")).thenReturn(List.of(remoteDepartment));

        List<StructureDepartmentDto> result = structureDepartmentService.findByStructure("090623");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodiceStruttura()).isEqualTo("090623");
        assertThat(result.get(0).getCodiceDisciplina()).isEqualTo("08");
        assertThat(result.get(0).getDisciplina()).isEqualTo("Cardiologia");
        assertThat(result.get(0).getIndirizzo()).isEqualTo("Via Risorgimento, 43");
    }

    @Test
    void saveShouldForwardDepartmentToRemoteService() {
        StructureDepartmentService structureDepartmentService = new StructureDepartmentService(healthStructureClient);
        StructureDepartmentDto dto = StructureDepartmentDto.builder()
                .codiceStruttura("090623")
                .codiceDisciplina("08")
                .disciplina("Cardiologia")
                .indirizzo("Via Risorgimento, 43")
                .build();
        when(healthStructureClient.createDepartment(any(StructureDepartmentSourceDto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StructureDepartmentDto saved = structureDepartmentService.save(dto);

        ArgumentCaptor<StructureDepartmentSourceDto> captor = ArgumentCaptor.forClass(StructureDepartmentSourceDto.class);
        verify(healthStructureClient).createDepartment(captor.capture());
        assertThat(captor.getValue().getCodiceStruttura()).isEqualTo("090623");
        assertThat(captor.getValue().getCodiceDisciplina()).isEqualTo("08");
        assertThat(saved.getCodiceStruttura()).isEqualTo("090623");
        assertThat(saved.getCodiceDisciplina()).isEqualTo("08");
        assertThat(saved.getDisciplina()).isEqualTo("Cardiologia");
    }
}