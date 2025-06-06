package kr.co.kimga.member.application.auth

import kr.co.kimga.member.application.auth.dto.AuthenticateRequestDto
import kr.co.kimga.member.domain.service.MemberAuthService
import kr.co.kimga.member.domain.service.SessionService
import kr.co.kimga.member.domain.service.TokenService
import kr.co.kimga.member.infrastructure.common.Utils
import kr.co.kimga.member.infrastructure.security.jwt.AccessJwtProvider
import kr.co.kimga.member.infrastructure.security.jwt.JwtProvider
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class AuthFacade (
    val sessionService: SessionService,
    val memberAuthService: MemberAuthService,
    val tokenService: TokenService
) {

    fun authenticate(authenticateRequest: AuthenticateRequestDto) : Pair<String, String> {
        val authenticatedMember = memberAuthService.authenticate(authenticateRequest.email, authenticateRequest.password)
        val uuid = Utils.generateUuid()
        val (accessToken, refreshToken) = tokenService.makeNewToken(uuid)
        sessionService.removeSession(authenticatedMember.id)
        sessionService.saveSession(accessToken, refreshToken, authenticatedMember.id, uuid)
        return Pair(accessToken, refreshToken)
    }

}