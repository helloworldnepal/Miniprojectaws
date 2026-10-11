package com.mini.ec2.service;

import com.mini.ec2.enums.InstanceStatus;
import com.mini.ec2.model.Instance;
import com.mini.ec2.repository.InstanceRepository;
import com.mini.ec2.service.impl.InstanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstanceServiceTest {
    @Mock
    private InstanceRepository instanceRepository;
    @Mock
    private DockerService dockerService;

    @InjectMocks
    private InstanceService instanceService;




    @Test
    void launchInstance_savesInstanceAndStartsContainer() {
        when(instanceRepository.save(any(Instance.class)))
                .thenAnswer(invocation -> {
                    Instance i = invocation.getArgument(0);
                    if (i.getId() == null) i.setId(1L);
                    return i;
                });
        when(dockerService.createAndStartContainer("nginx:latest", "web"))
                .thenReturn("container-123");

        Instance result = instanceService.launchInstance("web", "nginx:latest");

        assertThat(result.getContainerId()).isEqualTo("container-123");
        assertThat(result.getStatus()).isEqualTo(InstanceStatus.RUNNING);
        verify(instanceRepository, times(2)).save(any(Instance.class));
    }
}