package kr.co.kimga.member.domain.service

import lombok.RequiredArgsConstructor
import org.apache.kafka.common.protocol.types.Field.Bool
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SessionService (
    private val redisTemplate: RedisTemplate<String, Any>
) {
    companion object {
        private const val LOGIN_PREFIX = "login:"
        private const val SESSION_PREFIX = "session:"
    }

    fun saveSession(id: Long, uuid: String) {
        redisTemplate.opsForValue().set(LOGIN_PREFIX + id, uuid)
        redisTemplate.opsForValue().set(SESSION_PREFIX + uuid, id)
    }

    fun removeSessionByUuid(uuid: String) {
        val id = findMemberId(uuid)
        redisTemplate.delete(LOGIN_PREFIX + id)
        redisTemplate.delete(SESSION_PREFIX + uuid)
    }

    fun removeSessionById(id: Long) {
        val uuid = findUuid(id)
        redisTemplate.delete(LOGIN_PREFIX + id)
        redisTemplate.delete(SESSION_PREFIX + uuid)
    }

    fun hasSession(uuid: String): Boolean = redisTemplate.hasKey(SESSION_PREFIX + uuid)

    fun hasLogin(id: Long): Boolean = redisTemplate.hasKey(id.toString())

    fun findMemberId(uuid: String): Long? = (redisTemplate.opsForValue().get(SESSION_PREFIX + uuid) as Number?)?.toLong()
    fun findUuid(id: Long): String? = redisTemplate.opsForValue().get(LOGIN_PREFIX + id) as String?
}