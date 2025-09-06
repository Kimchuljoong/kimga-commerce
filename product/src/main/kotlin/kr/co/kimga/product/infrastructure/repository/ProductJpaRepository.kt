package kr.co.kimga.product.infrastructure.repository

import kr.co.kimga.product.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductJpaRepository : JpaRepository<Product, Long>