pipeline {
  agent {
    kubernetes {
      //cloud 'kubernetes'
      yaml """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug-539ddefcae3fd6b411a95982a830d987f4214251
    imagePullPolicy: Always
    command:
    - cat
    tty: true
    volumeMounts:
      - name: docker-config
        mountPath: /kaniko/.docker
  volumes:
    - name: docker-config
      configMap:
        name: docker-config
"""
    }
  }
  stages {
    stage('Build with Kaniko') {
      steps {
        git 'https://github.com/cheepo/kafka-app-demo'
        container('kaniko') {
            sh '''
            /kaniko/executor --dockerfile=/kaniko/workspace/kafka-app-demo/Dockerfile --context /kaniko/workspace/ --force --destination=cheepo/sample-microservice:latest --destination=cheepo/sample-microservice:v$BUILD_NUMBER
            '''
        }
      }
    }
  }
}
