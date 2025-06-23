package kr.co.kimga.member.infrastructure.docker

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.File
import java.net.Socket

@Configuration
@Profile("local")
class DockerRunner {

    private lateinit var dockerProcess: Process

    @PostConstruct
    fun startDockerCompose() {
        val processBuilder = ProcessBuilder("docker-compose", "up", "-d")
        processBuilder.directory(File(".docker"))
        processBuilder.redirectErrorStream(true)

        dockerProcess = processBuilder.start()
        dockerProcess.waitFor()

        repeat(10) {
            try {
                Socket("localhost", 6379).use { return }
            } catch (e: Exception) {
                Thread.sleep(1000)
            }
        }
    }

    @PreDestroy
    fun stopDockerCompose() {
        val processBuilder = ProcessBuilder("docker-compose", "down")
        processBuilder.directory(File(".docker"))
        processBuilder.redirectErrorStream(true)

        processBuilder.start().waitFor()
    }
}