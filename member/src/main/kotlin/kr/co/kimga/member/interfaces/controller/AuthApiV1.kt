package kr.co.kimga.member.interfaces.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.kimga.member.application.auth.dto.LoginRequestDto
import kr.co.kimga.member.application.auth.dto.TokenDto
import kr.co.kimga.member.interfaces.controller.dto.AuthRefreshRequestDto
import org.springframework.web.bind.annotation.*

@Tag(name = "인증 API V1")
@RequestMapping("/api/v1/auth")
interface AuthApiV1 {

    @Operation(summary = "회원 로그인")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "회원 로그인 성공"),
        ApiResponse(responseCode = "404", description = "로그인 실패"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequestDto: LoginRequestDto) : TokenDto

    @Operation(summary = "회원 토큰 갱신")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
        ApiResponse(responseCode = "400", description = "토큰이 유효하지 않음"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PutMapping("/refresh")
    fun refresh(@Valid @RequestBody authRefreshRequestDto: AuthRefreshRequestDto) : TokenDto

    @Operation(summary = "회원 로그아웃")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "회원 로그아웃 성공"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @DeleteMapping("/logout")
    fun logout()
}
