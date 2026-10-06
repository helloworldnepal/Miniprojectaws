package com.mini.ec2.repository;

import com.mini.ec2.enums.InstanceStatus;
import com.mini.ec2.model.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstanceRepository extends JpaRepository<Instance,Integer> {


    @Query("SELECT  i FROM Instance i where i.status<>:status")
    List<Instance> findAllByStatusNot(@Param("status") InstanceStatus status);


}
