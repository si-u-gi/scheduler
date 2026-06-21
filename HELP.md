[빌드 및 실행] {
DB 실행:
    cd h2/bin
    ./h2.sh

빌드 및 실행
    ./gradlew clean build
    cd build/libs
    java -jar marketplace-0.0.1-SNAPSHOT.jar
}

[자바 17 설치 및 적용 방법] {
자바 17 설치:
    sudo apt update
    sudo apt install openjdk-17-jdk

경로 수정:
    파일 열기
    nano ~/.bashrc

    맨 아래에 추가
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    export PATH=$JAVA_HOME/bin:$PATH

    적용
    source ~/.bashrc

.vscode.settings.json 파일에 추가:
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "/usr/lib/jvm/java-17-openjdk-amd64",
            "default": true
        }
    ],
    "java.import.gradle.java.home": "/usr/lib/jvm/java-17-openjdk-amd64",

확인:
    java -version
    javac -version
    chmod +x gradlew
    ./gradlew -version

빌드 해보기:
    ./gradlew clean build
}

[H2 DB 설치 및 적용 방법] {
    H2 DB 설치 및 압축 풀기:
        wget https://github.com/h2database/h2database/releases/download/version-2.4.240/h2-2025-09-22.zip
        unzip h2.zip
    
    파일 만들기():
    - Database "/home/codespace/test" not found, either pre-create it or allow remote database creation (not recommended in secure environments) [90149-240] 90149/90149 (Help)
    - 이 오류가 발생했을 때 해결가능
        mkdir -p /home/codespace
        touch /home/codespace/test.mv.db

    권환 주기 및 실행:
        chmod +x h2.sh
        ./h2.sh
}

[자동완성이 되지 않는다면] {
    Language Support for Java(TM) by Red Hat 이 확장을 껐다가 켜면 해결됨.
}

[스프링 이니셜라이져] {
    spring web, Thymeleaf
}