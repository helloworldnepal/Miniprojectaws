package com.mini.ec2.service;

import com.mini.ec2.enums.InstanceStatus;
import com.mini.ec2.model.Instance;
import com.mini.ec2.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstanceReconciliationService {
    private final InstanceRepository instanceRepository;
    private final DockerService dockerService;

    @Scheduled(fixedRate = 3000)
    public void reconcile() {

        List<Instance> allByStatusNot = instanceRepository.findAllByStatusNot(InstanceStatus.TERMINATED);
  for(Instance instance: allByStatusNot){
      if (instance.getContainerId()== null){

         continue;
      }

      String actualStatus = dockerService.getContainerStatus(instance.getContainerId());
      InstanceStatus mappedStatus = InstanceStatus.valueOf(actualStatus);
      if(instance.getStatus()!=mappedStatus){
          log.info("Reconciling instance {}: DB said {}, Docker says {}",
                  instance.getId(), instance.getStatus(), mappedStatus);

          instance.setStatus(mappedStatus);
          instanceRepository.save(instance);
      }

  }
    }


}
