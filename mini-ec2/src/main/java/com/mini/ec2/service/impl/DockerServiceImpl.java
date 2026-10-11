package com.mini.ec2.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.mini.ec2.exception.DockerOperationException;
import com.mini.ec2.service.DockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DockerServiceImpl implements DockerService {

    private static final long MEMORY_LIMIT_BYTES = 256 * 1024 * 1024L;
    private static final long CPU_QUOTA = 50000L;

    private final DockerClient dockerClient;

    @Override
    public String createAndStartContainer(String image, String name) {
        try {
            dockerClient.pullImageCmd(image).start().awaitCompletion();

            HostConfig hostConfig = HostConfig.newHostConfig()
                    .withMemory(MEMORY_LIMIT_BYTES)
                    .withCpuQuota(CPU_QUOTA);

            CreateContainerResponse container = dockerClient.createContainerCmd(image)
                    .withName(name + "-" + System.currentTimeMillis())
                    .withHostConfig(hostConfig)
                    .exec();

            dockerClient.startContainerCmd(container.getId()).exec();

            return container.getId();
        } catch (Exception e) {
            throw new DockerOperationException("Failed to create/start container for image: " + image, e);
        }
    }

    @Override
    public void stopContainer(String containerId) {
        try {
            dockerClient.stopContainerCmd(containerId).exec();
        } catch (Exception e) {
            throw new DockerOperationException("Failed to stop container: " + containerId, e);
        }
    }

    @Override
    public void startContainer(String containerId) {
        try {
            dockerClient.startContainerCmd(containerId).exec();
        } catch (Exception e) {
            throw new DockerOperationException("Failed to start container: " + containerId, e);
        }
    }

    @Override
    public void removeContainer(String containerId) {
        try {
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        } catch (Exception e) {
            throw new DockerOperationException("Failed to remove container: " + containerId, e);
        }
    }

    @Override
    public String getContainerStatus(String containerId) {
        try {
            InspectContainerResponse info = dockerClient.inspectContainerCmd(containerId).exec();
            Boolean running = info.getState().getRunning();
            return Boolean.TRUE.equals(running) ? "RUNNING" : "STOPPED";
        } catch (Exception e) {
            throw new DockerOperationException("Failed to inspect container: " + containerId, e);
        }
    }
}