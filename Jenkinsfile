@Library ('Shared') _
pipeline {
    agent any
    environment{
        GIT_URL = "https://github.com/Sourabh9125/chattingo.git/"
        GIT_BRANCH = "development"
        DOCKER_HUB_USERNAME = "sourabhlodhi"
        DOCKER_IMAGE_TAG = "V${BUILD_NUMBER}"
        DOCKER_FRONTEND_IMAGE = "chattingo-frontend"
        DOCKER_BACKEND_IMAGE = "chattingo-backend"
    }
    stages {
        stage('Clean Worksapce') {
            steps {
                script{
                    clean_ws()
                }
            }
        }
        stage('Code Cloning') {
            steps {
                script{
                    clone(env.GIT_URL, env.GIT_BRANCH)
                }
            }
        }
        stage("Build image"){
            parallel{
                 stage("Build Frontend Image"){
                    steps{
                        script{
                            docker_build(
                                imageName: env.DOCKER_FRONTEND_IMAGE,
                                imageTag: env.DOCKER_IMAGE_TAG,
                                context: "frontend",
                                dockerfile: "frontend/Dockerfile",
                                dockerHubUser: env.DOCKER_HUB_USERNAME
                                )
                        }
                    }
                
            }
                 stage("Build Backend Image"){
                   steps{
                       script{
                           docker_build(
                               imageName: env.DOCKER_BACKEND_IMAGE,
                               imageTag: env.DOCKER_IMAGE_TAG,
                               context: "backend",
                               dockerfile: "backend/Dockerfile",
                               dockerHubUser: env.DOCKER_HUB_USERNAME
                               )
                       }
                   }
               }
        }
    }
        // stage("Pushing to DockerHub"){
        //     parallel{
        //         stage("Pushing Frontend Image "){
        //             steps{
        //                 script{
        //                     docker_hub(
        //                         credentialsId: "DockerHubUser",
        //                         imageName: env.DOCKER_FRONTEND_IMAGE,
        //                         imageTag: env.DOCKER_IMAGE_TAG
        //                      )
        //                 }
        //             }
        //         }
        //         stage("Pushing Backend Image "){
        //             steps{
        //                 script{
        //                     docker_hub(
        //                         credentialsId: "DockerHubUser",
        //                         imageName: env.DOCKER_BACKEND_IMAGE,
        //                         imageTag: env.DOCKER_IMAGE_TAG
        //                      )
        //                 }
        //             }
        //         }
        //     }
        // }
        stage("Testing the code"){
            steps{
                script{
                    echo "Testing the code"
                }
            }
        }
        stage("Docker compose"){
            steps{
               script{
                   docker_compose()
               }
            }
        }
}
}
