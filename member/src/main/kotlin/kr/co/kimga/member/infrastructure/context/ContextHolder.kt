package kr.co.kimga.member.infrastructure.context

object ContextHolder {
    private val uuidHolder = ThreadLocal<String>()

    fun setUuid(uuid: String) = uuidHolder.set(uuid)
    fun getUuid(): String = uuidHolder.get() ?: throw NullPointerException()
    fun clear() = uuidHolder.remove()
}