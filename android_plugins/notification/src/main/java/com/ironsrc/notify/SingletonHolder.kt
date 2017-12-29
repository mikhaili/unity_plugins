package com.ironsrc.notify

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val inst = instance

            when (inst) {
                null -> {
                    val created = creator!!(arg)
                    instance = created
                    creator = null
                    created
                }
                else -> inst
            }
        }
    }
}