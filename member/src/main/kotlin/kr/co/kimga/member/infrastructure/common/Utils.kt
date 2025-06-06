package kr.co.kimga.member.infrastructure.common

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class Utils {

    companion object {
        fun generateUuid() : String {
            return UUID.randomUUID().toString()
        }
    }
}