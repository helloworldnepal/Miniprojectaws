package com.mini.ec2.service.impl;

import com.mini.ec2.enums.InstanceStatus;
import com.mini.ec2.exception.InstanceNotFoundException;
import com.mini.ec2.model.Instance;
import com.mini.ec2.repository.InstanceRepository;
import com.mini.ec2.service.DockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstanceService {


    private final InstanceRepository instanceRepository;
    private final DockerService dockerService;


    public Instance launchInstance(String name,String image){

          Instance instance=Instance.builder()
                  .name(name)
                  .image(image)
                  .status(InstanceStatus.PENDING)
                  .build();

          instance=instanceRepository.save(instance);

        String container = dockerService.createAndStartContainer(image, name);

        instance.setContainerId(container);
        instance.setStatus(InstanceStatus.RUNNING);
        return instanceRepository.save(instance);

    }
    public List<Instance> listInstances() {
        return instanceRepository.findAll();
    }


    public Instance getInstanceOrThrow(Integer id) {
        return instanceRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(id));
    }

    public Instance stopInstance(Integer id) {

            Instance instance = getInstanceOrThrow(id);
            dockerService.stopContainer(instance.getContainerId());
            instance.setStatus(InstanceStatus.STOPPED);
            return instanceRepository.save(instance);
        }

    public Instance startInstance(Integer id) {
        Instance instance = getInstanceOrThrow(id);
        dockerService.startContainer(instance.getContainerId());
        instance.setStatus(InstanceStatus.RUNNING);
        return instanceRepository.save(instance);
    }

    public void terminateInstance(Integer id) {
        Instance instance = getInstanceOrThrow(id);
        dockerService.removeContainer(instance.getContainerId());
        instance.setStatus(InstanceStatus.TERMINATED);
        instanceRepository.save(instance);
    }
    }

