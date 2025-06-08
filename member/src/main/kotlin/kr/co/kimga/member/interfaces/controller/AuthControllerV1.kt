package kr.co.kimga.member.interfaces.controller

import jakarta.validation.Valid
import kr.co.kimga.member.application.auth.AuthFacade
import kr.co.kimga.member.application.auth.dto.LoginRequestDto
import kr.co.kimga.member.application.auth.dto.TokenDto
import kr.co.kimga.member.application.auth.dto.LogoutRequestDto
import kr.co.kimga.member.application.auth.dto.RefreshRequestDto
import kr.co.kimga.member.infrastructure.context.ContextHolder
import kr.co.kimga.member.interfaces.controller.dto.AuthRefreshRequestDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthControllerV1 (
    private val authFacade: AuthFacade
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequestDto: LoginRequestDto) : TokenDto {
        return authFacade.login(loginRequestDto)
    }

    @PutMapping("/refresh")
    fun refresh(@Valid @RequestBody authRefreshRequestDto: AuthRefreshRequestDto) : TokenDto {
        return authFacade.refresh(RefreshRequestDto(authRefreshRequestDto.refreshToken, ContextHolder.getUuid()))
    }

    @DeleteMapping("/logout")
    fun logout() {
        authFacade.logout(LogoutRequestDto(ContextHolder.getUuid()))
    }


}