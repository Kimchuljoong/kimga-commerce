package kr.co.kimga.member.interfaces.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.kimga.member.application.member.MemberFacade
import kr.co.kimga.member.application.member.dto.JoinRequestDto
import kr.co.kimga.member.application.member.dto.ModifyRequestDto
import kr.co.kimga.member.application.member.dto.WithdrawalRequestDto
import kr.co.kimga.member.infrastructure.context.MemberContextHolder
import kr.co.kimga.member.interfaces.controller.dto.MemberCreateRequestDto
import kr.co.kimga.member.interfaces.controller.dto.MemberModifyRequestDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "회원 API V1")
class MemberControllerV1(
    private val memberFacade: MemberFacade
) {

    @Operation(summary = "회원 생성")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "회원 신규 생성 정상"),
        ApiResponse(responseCode = "409", description = "이미 생성된 회원 정보"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody memberCreateRequestDto: MemberCreateRequestDto) =
        memberFacade.join(JoinRequestDto(
            email = memberCreateRequestDto.email,
            password = memberCreateRequestDto.password,
            name = memberCreateRequestDto.name,
        ))

    @Operation(summary = "회원 정보 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "회원 정보 수정 정상"),
        ApiResponse(responseCode = "400", description = "수정 정보 정합성 문제"),
        ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
        )
    @PutMapping("")
    fun update(@Valid @RequestBody memberModifyRequestDto: MemberModifyRequestDto) =
        memberFacade.modify(ModifyRequestDto(MemberContextHolder.getContext().id, memberModifyRequestDto.name))

    @Operation(summary = "회원 삭제")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "회원 삭제 정상"),
        ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @DeleteMapping("")
    fun delete() {
        memberFacade.withdrawal(WithdrawalRequestDto(MemberContextHolder.getContext().id))
    }
}