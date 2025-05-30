package com.elkhami.f1champions.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

inline fun <reified T> T.loggerWithPrefix(prefix: String = "[DEV]"): PrefixedLogger {
    return PrefixedLogger(LoggerFactory.getLogger(T::class.java), prefix)
}
