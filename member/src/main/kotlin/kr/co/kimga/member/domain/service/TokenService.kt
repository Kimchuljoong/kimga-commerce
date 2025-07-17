package kr.co.kimga.member.domain.service

import io.jsonwebtoken.Claims
import kr.co.kimga.member.domain.exception.CanNotRenewTokenException
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

    fun renewToken(oldRefreshToken: String, uuid: String) : Pair<String, String> {
        if (canRefresh(oldRefreshToken)) {
            val newAccessToken = accessJwtProvider.generate(uuid)
            val newRefreshToken = refreshJwtProvider.generate(uuid)
            return Pair(newAccessToken, newRefreshToken)
        }
        throw CanNotRenewTokenException()
    }

    fun canRefresh(refreshToken: String) : Boolean {
        val refreshValidationResult = refreshJwtProvider.validate(refreshToken)
        return when {
            refreshValidationResult == JwtValidationResult.VALID -> true
            else -> false
        }
    }

    fun getClaims(token: String): Claims = accessJwtProvider.extractClaims(token)
}