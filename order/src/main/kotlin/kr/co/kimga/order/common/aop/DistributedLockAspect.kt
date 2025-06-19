package kr.co.kimga.order.common.aop

import kr.co.kimga.order.common.annotation.DistributedLock
import lombok.RequiredArgsConstructor
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Aspect
@Component
@RequiredArgsConstructor
class DistributedLockAspect(
    private val redissonClient: RedissonClient
) {

    private val parser = SpelExpressionParser()

    @Around("@annotation(lock)")
    @Throws(Throwable::class)
    fun lock(joinPoint: ProceedingJoinPoint, lock: DistributedLock): Any? {
        val key = parseKey(lock.key, joinPoint)
        val rLock: RLock = redissonClient.getLock(key)

        var acquired = false

        return try {
            acquired = rLock.tryLock(lock.waitTime, lock.lockTime, TimeUnit.SECONDS)
            if (!acquired) {
                throw IllegalStateException("Unable to get Redis lock for key: $key")
            }
            joinPoint.proceed()
        } finally {
            if (acquired && rLock.isHeldByCurrentThread) {
                rLock.unlock()
            }
        }
    }

    private fun parseKey(rawKey: String, joinPoint: ProceedingJoinPoint): String {
        val method = (joinPoint.signature as MethodSignature).method
        val paramNames = method.parameters.map { it.name }
        val paramValues = joinPoint.args

        val context = StandardEvaluationContext()
        paramNames.forEachIndexed { index, name ->
            context.setVariable(name, paramValues[index])
        }

        return parser.parseExpression(rawKey).getValue(context, String::class.java)
            ?: throw IllegalArgumentException("Failed to parse SpEL key: $rawKey")
    }

}