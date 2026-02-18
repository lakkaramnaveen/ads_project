pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'your-docker-registry'
        KUBERNETES_NAMESPACE = 'ads-services'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    def services = ['discovery-service', 'campaign-service', 'ad-serving-service', 
                                   'analytics-service', 'user-targeting-service', 'api-gateway']
                    
                    services.each { service ->
                        sh """
                            docker build -t ${DOCKER_REGISTRY}/${service}:${env.BUILD_NUMBER} ./${service}
                            docker tag ${DOCKER_REGISTRY}/${service}:${env.BUILD_NUMBER} ${DOCKER_REGISTRY}/${service}:latest
                        """
                    }
                }
            }
        }
        
        stage('Docker Push') {
            steps {
                script {
                    sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS ${DOCKER_REGISTRY}'
                    sh 'docker push ${DOCKER_REGISTRY}/*:${BUILD_NUMBER}'
                    sh 'docker push ${DOCKER_REGISTRY}/*:latest'
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                sh 'mvn org.owasp:dependency-check-maven:check'
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                sh """
                    kubectl set image deployment/discovery-service discovery-service=${DOCKER_REGISTRY}/discovery-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/campaign-service campaign-service=${DOCKER_REGISTRY}/campaign-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/ad-serving-service ad-serving-service=${DOCKER_REGISTRY}/ad-serving-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/analytics-service analytics-service=${DOCKER_REGISTRY}/analytics-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/user-targeting-service user-targeting-service=${DOCKER_REGISTRY}/user-targeting-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/api-gateway api-gateway=${DOCKER_REGISTRY}/api-gateway:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                """
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                input message: 'Deploy to Production?', ok: 'Deploy'
                sh """
                    kubectl set image deployment/discovery-service discovery-service=${DOCKER_REGISTRY}/discovery-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/campaign-service campaign-service=${DOCKER_REGISTRY}/campaign-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/ad-serving-service ad-serving-service=${DOCKER_REGISTRY}/ad-serving-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/analytics-service analytics-service=${DOCKER_REGISTRY}/analytics-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/user-targeting-service user-targeting-service=${DOCKER_REGISTRY}/user-targeting-service:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                    kubectl set image deployment/api-gateway api-gateway=${DOCKER_REGISTRY}/api-gateway:${env.BUILD_NUMBER} -n ${KUBERNETES_NAMESPACE}
                """
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            cleanWs()
        }
    }
}
