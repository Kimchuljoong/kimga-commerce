package kr.co.kimga.member.interfaces.controller

import kr.co.kimga.member.application.auth.AuthFacade
import kr.co.kimga.member.application.auth.dto.LoginRequestDto
import kr.co.kimga.member.application.auth.dto.LogoutRequestDto
import kr.co.kimga.member.application.auth.dto.RefreshRequestDto
import kr.co.kimga.member.application.auth.dto.TokenDto
import kr.co.kimga.member.infrastructure.context.MemberContextHolder
import kr.co.kimga.member.interfaces.controller.dto.AuthRefreshRequestDto
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthControllerV1(
    private val authFacade: AuthFacade
) : AuthApiV1 {

    override fun login(loginRequestDto: LoginRequestDto): TokenDto {
        return authFacade.login(loginRequestDto)
    }

    override fun refresh(authRefreshRequestDto: AuthRefreshRequestDto): TokenDto {
        return authFacade.refresh(
            RefreshRequestDto(
                authRefreshRequestDto.refreshToken,
                MemberContextHolder.getContext().uuid
            )
        )
    }

    override fun logout() {
        authFacade.logout(
            LogoutRequestDto(MemberContextHolder.getContext().uuid)
        )
    }
}
