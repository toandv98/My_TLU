package com.toandv.mytlu.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_mark")
data class DetailMark(
    val maMon: String,
    val tenMon: String,
    val soTin: Int,
    val quaTrinh: Float,
    val diemThi: Float,
    val tongKet: Float,
    val diemChu: Char,
    var hocKiGanNhat: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}