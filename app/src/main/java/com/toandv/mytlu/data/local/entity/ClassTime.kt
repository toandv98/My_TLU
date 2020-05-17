package com.toandv.mytlu.data.local.entity

import org.joda.time.LocalDateTime

data class ClassTime(
    val fromPeriod: Int,
    val toPeriod: Int,
    val classTime: LocalDateTime
)