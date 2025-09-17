package com.devlog.remember.utils

import kotlin.concurrent.Volatile

interface AnalyticsDelegate {
    /** propsJson은 JSON 문자열 (없으면 null) */
    fun track(event: String, propsJson: String? = null)
}

object Analytics {
    @Volatile
    private var delegate: AnalyticsDelegate? = null

    fun install(delegate: AnalyticsDelegate) { this.delegate = delegate }

    /** Kotlin 쪽에서는 Map을 받아 JSON으로 변환해서 delegate에 넘김 */
    fun track(event: String, props: Map<String, Any?> = emptyMap()) {
        val json = props.toJsonOrNull()
        delegate?.track(event, json)
    }
}

/* 매우 단순한 JSON 인코더 (문자열/숫자/불리언/중첩 Map/리스트 정도 지원) */
private fun Map<String, Any?>.toJsonOrNull(): String? {
    if (isEmpty()) return null
    fun enc(v: Any?): String = when (v) {
        null -> "null"
        is String -> "\"" + v.replace("\"", "\\\"") + "\""
        is Number, is Boolean -> v.toString()
        is Map<*, *> -> (v as? Map<String, Any?>)?.toJsonOrNull() ?: "null"
        is Iterable<*> -> v.joinToString(prefix = "[", postfix = "]") { enc(it) }
        else -> "\"" + v.toString().replace("\"", "\\\"") + "\""
    }
    return entries.joinToString(prefix = "{", postfix = "}") { "\"${it.key}\":${enc(it.value)}" }
}
