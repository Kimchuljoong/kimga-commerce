package kr.co.kimga.member.domain.service

import io.jsonwebtoken.Claims
import kr.co.kimga.member.domain.exception.CanNotRenewTokenException
import kr.co.kimga.member.infrastructure.common.Utils
import kr.co.kimga.member.infrastructure.security.jwt.AccessJwtProvider
import kr.co.kimga.member.infrastructure.security.jwt.JwtProvider
import kr.co.kimga.member.infrastructure.security.jwt.JwtValidationResult
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class TokenService (
    @Qualifier("accessJwtProvider")
    private val accessJwtProvider: JwtProvider,
    @Qualifier("refreshJwtProvider")
    private val refreshJwtProvider: JwtProvider
) {

    fun makeNewToken(uuid: String) : Pair<String, String> {
        val accessToken = accessJwtProvider.generate(uuid)
        val refreshToken = refreshJwtProvider.generate(uuid)
        return Pair(accessToken, refreshToken)
    }

    fun renewToken(oldAccessToken: String, oldRefreshToken: String) : Pair<String, String> {
        if (canRefresh(oldAccessToken, oldRefreshToken)) {
            val accessUuid = Utils.generateUuid()
            val refreshUuid = Utils.generateUuid()
            val newAccessToken = accessJwtProvider.generate(accessUuid)
            val newRefreshToken = refreshJwtProvider.generate(refreshUuid)
            return Pair(newAccessToken, newRefreshToken)
        }
        throw CanNotRenewTokenException()
    }

    fun canRefresh(accessToken: String, refreshToken: String) : Boolean {
        val accessValidationResult = accessJwtProvider.validate(accessToken)
        val refreshValidationResult = refreshJwtProvider.validate(refreshToken)
        return when {
            accessValidationResult.equals(JwtValidationResult.EXPIRED) &&
            refreshValidationResult.equals(JwtValidationResult.VALID) -> true
            else -> false
        }
    }

    fun getSubject(token: String): Claims = accessJwtProvider.extractSubject(token)
}