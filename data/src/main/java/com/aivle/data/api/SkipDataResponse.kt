package com.aivle.data.api

import java.util.function.BiConsumer

class SkipDataResponse : Map<String, String> {

    val data: MutableMap<String, String> = mutableMapOf()

    override val entries: Set<Map.Entry<String, String>>
        get() = data.entries
    override val keys: Set<String>
        get() = data.keys
    override val size: Int
        get() = data.size
    override val values: Collection<String>
        get() = data.values

    override fun containsKey(key: String): Boolean = data.containsKey(key)

    override fun containsValue(value: String): Boolean = data.containsValue(value)

    override fun forEach(action: BiConsumer<in String, in String>) = data.forEach(action)

    override fun get(key: String): String? = data[key]

    override fun getOrDefault(key: String, defaultValue: String): String = data.getOrDefault(key, defaultValue)

    override fun isEmpty(): Boolean = data.isEmpty()
}