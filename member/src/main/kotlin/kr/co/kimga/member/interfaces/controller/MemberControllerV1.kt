package kr.co.kimga.member.interfaces.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/member")
class MemberControllerV1 {

    @PostMapping("")
    fun create() {

    }

    @PutMapping("/{uuid}")
    fun update(@PathVariable("uuid") uuid: String) {

    }

    @DeleteMapping("")
    fun delete() {

    }
}