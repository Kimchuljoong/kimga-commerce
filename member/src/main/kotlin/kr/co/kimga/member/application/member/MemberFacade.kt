package kr.co.kimga.member.application.member

import kr.co.kimga.member.application.member.dto.JoinRequestDto
import kr.co.kimga.member.application.member.dto.ModifyRequestDto
import kr.co.kimga.member.application.member.dto.WithdrawalRequestDto
import kr.co.kimga.member.domain.dto.CreateMemberDto
import kr.co.kimga.member.domain.dto.ModifyMemberDto
import kr.co.kimga.member.domain.dto.WithdrawMemberDto
import kr.co.kimga.member.domain.service.MemberService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class MemberFacade (
    private val memberService: MemberService,
) {

    @Transactional
    fun join(joinRequestDto: JoinRequestDto) {
        val request = CreateMemberDto(
            email = joinRequestDto.email,
            password = joinRequestDto.password,
            name = joinRequestDto.name,
        )
        memberService.create(request)
    }

    @Transactional
    fun modify(modifyRequestDto: ModifyRequestDto) {
        val request = ModifyMemberDto(
            id = modifyRequestDto.id,
            name = modifyRequestDto.name
        )
        memberService.modify(request)
    }

    @Transactional
    fun withdrawal(withdrawalRequestDto: WithdrawalRequestDto) {
        val request = WithdrawMemberDto(
            id = withdrawalRequestDto.id,
        )
        memberService.withDraw(request)
    }


}