#!/usr/bin/env groovy

pipeline {
    agent any

    environment {
        DOCKER_REPO = 'tsemb/demo-app'
        DOCKER_REPO_SERVER = 'docker.io'
    }

    tools {
        maven 'Maven'
    }

    stages {
        stage('increment version') {
            steps {
                script {
                    echo 'incrementing app version...'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
                }
            }
        }

        stage('build app') {
            steps {
                script {
                    echo 'building the application...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('build image') {
            steps {
                script {
                    echo "building the docker image..."
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
                        sh 'echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}'
                        sh "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
                    }
                }
            }
        }

        stage('deploy to K8s EKS') {
            environment {
                AWS_ACCESS_KEY_ID = credentials('aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('aws_secret_access_key')
                APP_NAME = 'java-maven-app'
            }
            steps {
                script {
                    echo 'deploying docker image...'
                    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
                    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
                }
            }
        }

        stage('commit version update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh '''
                            git config user.name "Arno"
                            git config user.email "arno@example.com"
                            git remote set-url origin https://$USER:$PASS@github.com/TsembA/CI-CD-Pipeline-with-Jenkins-EKS-and-DockerHub.git
                            git add .
                            git commit -m "ci: version bump"
                            git push origin HEAD:master
                        '''
                    }
                }
            }
        }
    }
}
