package com.elkhami.f1champions.utils

import org.slf4j.Logger

class PrefixedLogger(
    private val logger: Logger,
    private val prefix: String,
) {
    fun info(msg: String) = logger.info("$prefix $msg")
}
