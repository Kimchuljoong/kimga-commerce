package kr.co.kimga.member.interfaces.controller

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
class MemberControllerV1(
    private val memberFacade: MemberFacade
) {

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody memberCreateRequestDto: MemberCreateRequestDto) =
        memberFacade.join(JoinRequestDto(
            email = memberCreateRequestDto.email,
            password = memberCreateRequestDto.password,
            name = memberCreateRequestDto.name,
        ))

    @PutMapping("")
    fun update(@Valid @RequestBody memberModifyRequestDto: MemberModifyRequestDto) =
        memberFacade.modify(ModifyRequestDto(MemberContextHolder.getContext().id, memberModifyRequestDto.name))

    @DeleteMapping("")
    fun delete() {
        memberFacade.withdrawal(WithdrawalRequestDto(MemberContextHolder.getContext().id))
    }
}