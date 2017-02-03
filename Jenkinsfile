node('master') {
  // stage 'Build and Test'
  def mvnHome = tool 'maven3'
  env.JAVA_HOME = tool 'jdk7'
  env.PATH = "${mvnHome}/bin:./:${env.PATH}"
  checkout scm

  stage ('Build cas-server-extension-duo') {
    sh 'mvn clean package install'
    archiveArtifacts artifacts: 'target/cas-server-extension-duo-*.jar'
  }
}
