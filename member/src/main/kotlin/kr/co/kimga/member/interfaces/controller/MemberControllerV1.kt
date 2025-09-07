package kr.co.kimga.member.interfaces.controller

import kr.co.kimga.member.application.member.MemberFacade
import kr.co.kimga.member.application.member.dto.JoinRequestDto
import kr.co.kimga.member.application.member.dto.ModifyRequestDto
import kr.co.kimga.member.application.member.dto.WithdrawalRequestDto
import kr.co.kimga.member.infrastructure.context.MemberContextHolder
import kr.co.kimga.member.interfaces.controller.dto.MemberCreateRequestDto
import kr.co.kimga.member.interfaces.controller.dto.MemberModifyRequestDto
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberControllerV1(
    private val memberFacade: MemberFacade
) : MemberApiV1 {

    override fun create(memberCreateRequestDto: MemberCreateRequestDto) =
        memberFacade.join(
            JoinRequestDto(
                email = memberCreateRequestDto.email,
                password = memberCreateRequestDto.password,
                name = memberCreateRequestDto.name,
            )
        )

    override fun update(memberModifyRequestDto: MemberModifyRequestDto) =
        memberFacade.modify(
            ModifyRequestDto(
                MemberContextHolder.getContext().id,
                memberModifyRequestDto.name
            )
        )

    override fun delete() {
        memberFacade.withdrawal(
            WithdrawalRequestDto(MemberContextHolder.getContext().id)
        )
    }
}
