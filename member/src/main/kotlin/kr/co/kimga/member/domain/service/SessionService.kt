package kr.co.kimga.member.domain.service

import lombok.RequiredArgsConstructor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
@RequiredArgsConstructor
class SessionService (
    val redisTemplate: RedisTemplate<String, Any>
) {
    private val SESSION_PREFIX = "refresh:"

    fun saveSession(session: Any, id: String) {
        redisTemplate.opsForValue().set(getRedisKey(id), session, Duration.ofMinutes(1))
    }

    fun removeSession(id: String) {
        redisTemplate.delete(getRedisKey(id))
    }

    fun hasSession(id: String): Boolean {
        return redisTemplate.hasKey(getRedisKey(id))
    }

    private fun getRedisKey(id: String) = SESSION_PREFIX + id
}