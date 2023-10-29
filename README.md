## docker

## jenkins

#### Jenkins image pull
`docker pull jenkins/jenkins:lts`

#### Jenkins run
`docker run --name jenkins-docker -d -p 8080:8080 -p 50000:50000 -v /home/jenkins:/var/jenkins_home -u root jenkins/jenkins:lts`

## boot service

#### Dockerfile settings
```dockerfile
FROM ubuntu:20.04

# default user
ENV USER boot

# packages install
RUN apt-get update && apt-get upgrade -y && \
    apt-get install -y sudo vim net-tools ssh openssh-server openjdk-17-jdk-headless

# Access Option
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -i 's/UsePAM yes/#UserPAM yes/g' /etc/ssh/sshd_config

#user add & set
RUN groupadd -g 999 $USER
RUN useradd -m -r -u 999 -g $USER $USER

RUN sed -ri '20a'$USER'    ALL=(ALL) NOPASSWD:ALL' /etc/sudoers

#set root & user passwd
RUN echo 'root:root' | root
RUN echo $USER':boot' | boot

# java 환경변수
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

ENTRYPOINT sudo service ssh restart && bash

USER $USER
```

#### Dockerfile build
`docker build -t boot-service .`

#### boot-service run
`docker run --name boot-service -itd -p 9000:9000 -p 9022:22 boot-service`

#### boot-service run shell script
```shell
JAR_PATH="/home/boot/boot_service/target/jenkins-with-docker-0.0.1-SNAPSHOT.jar"
PROCESS_NAME="jenkins-with-docker"

start() {
  echo "Deploy Project...."
  nohup java -jar $JAR_PATH > /dev/null 2>&1 &
  echo "Done"
}

stop() {
  echo "PID Check..."
  CURRENT_PID=$(ps -ef | grep java | grep $PROCESS_NAME | awk '{print $2}')
  echo "Running PID: $CURRENT_PID"

  if [ -z "$CURRENT_PID" ]; then
     echo "Project is not running"
  else
     echo "Project is stopping"
     kill -9 $CURRENT_PID
     sleep 3
  fi
}

restart() {
  stop
  sleep 5
  start
}

case $1 in
start)
    start
    ;;
stop)
    stop
    ;;
restart)
    restart
    ;;
*)
   echo "Usage: ./run.sh {start|stop|restart}"
esac
```