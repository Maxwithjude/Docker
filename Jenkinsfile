pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                script {
                    def branchName = env.GIT_BRANCH

                    if (branchName.contains('dev/fe')) {
                        echo "프론트엔드 작업 실행"
                        docker.image('node:22').inside {
                            sh '''
                                cd vue-app
                                npm install
                                npm run build
                            '''
                        }
                    } else if (branchName.contains('dev/be')) {
                        echo "백엔드 작업 실행"
                        // 백엔드 빌드 스크립트 추가
                    } else {
                        error "알 수 없는 브랜치: ${branchName}"
                    }
                }
            }
        }
    }
}
