pipeline {
  agent {
    kubernetes {
      //cloud 'kubernetes'
      yaml """
kind: Pod
metadata:
  name: img
spec:
  containers:
  - name: img
    image: jessfraz/img
    imagePullPolicy: Always
    command:
    - cat
    tty: true
    volumeMounts:
      - name: docker-config
        mountPath: /home/user/.docker
  volumes:
    - name: docker-config
      persistentVolumeClaim:
        claimName: jenkins-build
"""
    }
  }
  stages {
    stage('Build with Img') {
      environment {
        PATH = "/home/user/bin:$PATH"
      }
      steps {
        git 'https://github.com/cheepo/kafka-app-demo'
        container(name: 'img') {
            sh '''
            img build . -t cheepo/peepo
            '''
            sh ' img push cheepo/peepo:latest'
        }
      }
    }
  }
}
