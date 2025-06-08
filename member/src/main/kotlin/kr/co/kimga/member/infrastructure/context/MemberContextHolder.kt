package kr.co.kimga.member.infrastructure.context

object MemberContextHolder {
    private val uuidHolder = ThreadLocal<MemberContext>()

    fun setContext(context: MemberContext) = uuidHolder.set(context)
    fun getContext(): MemberContext = uuidHolder.get() ?: throw NullPointerException()
    fun clear() = uuidHolder.remove()
}