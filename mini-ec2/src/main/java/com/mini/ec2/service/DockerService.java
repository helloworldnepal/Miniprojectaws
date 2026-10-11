package com.mini.ec2.service;

public interface DockerService {
    String createAndStartContainer(String image, String name);

    void stopContainer(String containerId);

    void startContainer(String containerId);

    void removeContainer(String containerId);

    String getContainerStatus(String containerId);
}
