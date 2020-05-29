package com.toandv.mytlu.remote.domain

import org.joda.time.LocalDateTime

data class ClassTime(
    val fromPeriod: Int,
    val toPeriod: Int,
    val classTime: LocalDateTime
)