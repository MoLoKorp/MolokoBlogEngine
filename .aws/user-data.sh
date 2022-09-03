#!/bin/bash
echo ECS_CLUSTER=Test >> /etc/ecs/ecs.config
echo '{
  "log-driver": "awslogs",
  "log-opts": {
    "awslogs-region": "eu-north-1",
    "awslogs-group" : "moloko"
  }
}' > /etc/docker/daemon.json
yum update -y
yum install -y awslogs
systemctl start awslogsd
