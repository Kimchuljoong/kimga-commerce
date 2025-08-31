package kr.co.kimga.product.interfaces.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.kimga.product.domain.entity.enums.ProductStatus
import kr.co.kimga.product.infrastructure.service.dto.ProductDto
import kr.co.kimga.product.interfaces.controller.dto.ProductEnrollRequest
import kr.co.kimga.product.interfaces.controller.dto.ProductUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*

@Tag(name = "상품 API V1")
@RequestMapping("/api/v1/product")
interface ProductApiV1 {

    @Operation(summary = "상품 목록 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "상품 목록 정상 조회"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @GetMapping("/products")
    fun getProducts(
        @RequestParam(required = false) productName: String = "",
        @RequestParam(required = false) productStatus: ProductStatus?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<ProductDto>

    @Operation(summary = "상품 정보 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "상품 정보 정상 조회"),
        ApiResponse(responseCode = "404", description = "상품 정보 미존재"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable("productId") productId: Long): ProductDto

    @Operation(summary = "상품 신규 등록")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "상품 생성 정상"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("")
    fun enrollProduct(
        @RequestBody @Valid productEnrollRequest: ProductEnrollRequest
    )

    @Operation(summary = "상품 정보 변경")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "상품 정보 정상 변경"),
        ApiResponse(responseCode = "400", description = "상품 정보 변경 불가"),
        ApiResponse(responseCode = "404", description = "상품 정보 미존재"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable("productId") productId: Long,
        @RequestBody @Valid productUpdateRequest: ProductUpdateRequest
    )

    @Operation(summary = "상품 판매 상태로 전환")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "판매 상태로 정상 변경"),
        ApiResponse(responseCode = "400", description = "판매 상태로 변경 불가"),
        ApiResponse(responseCode = "404", description = "상품 정보 미존재"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("/{productId}/sale")
    fun saleProduct(@PathVariable("productId") productId: Long)

    @Operation(summary = "상품 판매 중지 상태로 전환")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "판매 중지 상태로 정상 변경"),
        ApiResponse(responseCode = "400", description = "판매 중지 상태로 변경 불가"),
        ApiResponse(responseCode = "404", description = "상품 정보 미존재"),
        ApiResponse(responseCode = "500", description = "내부 서버 오류")
    )
    @PostMapping("/{productId}/close")
    fun closeProduct(@PathVariable("productId") productId: Long)
}
