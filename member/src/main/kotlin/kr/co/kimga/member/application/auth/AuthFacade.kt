package kr.co.kimga.member.application.auth

import kr.co.kimga.member.application.auth.dto.LoginRequestDto
import kr.co.kimga.member.application.auth.dto.LogoutRequestDto
import kr.co.kimga.member.application.auth.dto.RefreshRequestDto
import kr.co.kimga.member.domain.exception.CanNotRefreshTokenException
import kr.co.kimga.member.domain.service.MemberAuthService
import kr.co.kimga.member.domain.service.SessionService
import kr.co.kimga.member.domain.service.TokenService
import kr.co.kimga.member.infrastructure.common.Utils
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AuthFacade (
    val sessionService: SessionService,
    val memberAuthService: MemberAuthService,
    val tokenService: TokenService
) {

    fun login(loginRequest: LoginRequestDto) : Pair<String, String> {
        val authenticatedMember = memberAuthService.authenticate(loginRequest.email, loginRequest.password)
        val uuid = Utils.generateUuid()
        val (accessToken, refreshToken) = tokenService.makeNewToken(uuid)
        sessionService.removeSessionById(authenticatedMember.id)
        sessionService.saveSession(authenticatedMember.id, uuid)
        return Pair(accessToken, refreshToken)
    }

    fun logout(logoutRequestDto: LogoutRequestDto) {
        if (sessionService.hasSession(logoutRequestDto.uuid)) {
            sessionService.removeSessionByUuid(logoutRequestDto.uuid)
        }
    }

    fun refresh(refreshRequestDto: RefreshRequestDto) : Pair<String, String> {
        if (!sessionService.hasSession(refreshRequestDto.uuid)) {
            throw CanNotRefreshTokenException()
        }
        val id = sessionService.findMemberId(refreshRequestDto.uuid)
        val uuid = Utils.generateUuid()
        val (newAccessToken, newRefreshToken) = tokenService.renewToken(refreshRequestDto.accessToken, refreshRequestDto.refreshToken, uuid)
        sessionService.removeSessionByUuid(refreshRequestDto.uuid)
        sessionService.saveSession(id, uuid)
        return Pair(newAccessToken, newRefreshToken)
    }

}