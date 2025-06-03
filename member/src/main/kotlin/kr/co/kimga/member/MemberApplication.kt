package kr.co.kimga.member

import config.H2PrdGuard
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(H2PrdGuard::class)
class MemberApplication

fun main(args: Array<String>) {
    runApplication<MemberApplication>(*args)
}