package kr.co.kimga.member.domain.service

import lombok.RequiredArgsConstructor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SessionService (
    val redisTemplate: RedisTemplate<String, Any>
) {
    private val ACCESS_PREFIX = "access:"
    private val REFRESH_PREFIX = "refresh:"

    fun saveSession(accessToken: String, refreshToken: String, id: Long, uuid: String) {
        redisTemplate.opsForValue().set(uuid, id)
        redisTemplate.opsForValue().set(id.toString(), uuid)
        redisTemplate.opsForValue().set(ACCESS_PREFIX + uuid, accessToken)
        redisTemplate.opsForValue().set(REFRESH_PREFIX + uuid, refreshToken)
    }

    fun removeSession(id: Long) {
        val uuid = redisTemplate.opsForValue().get(id.toString()).toString()
        redisTemplate.delete(id.toString())
        redisTemplate.delete(uuid)
        redisTemplate.delete(ACCESS_PREFIX + uuid)
        redisTemplate.delete(REFRESH_PREFIX + uuid)
    }

    fun hasSession(key: String): Boolean = redisTemplate.hasKey(key)

    private fun findMemberId(uuid: String): String = redisTemplate.opsForValue().get(uuid).toString()
}