package kr.co.kimga.member.infrastructure.docker

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.File

@Configuration
@Profile("local | test")
class DockerRunner {

    private lateinit var dockerProcess: Process

    @PostConstruct
    fun startDockerCompose() {
        val processBuilder = ProcessBuilder("docker-compose", "up", "-d")
        processBuilder.directory(File(".docker"))
        processBuilder.redirectErrorStream(true)

        dockerProcess = processBuilder.start()
    }

    @PreDestroy
    fun stopDockerCompose() {
        val processBuilder = ProcessBuilder("docker-compose", "down")
        processBuilder.directory(File(".docker"))
        processBuilder.redirectErrorStream(true)

        processBuilder.start().waitFor()
    }
}