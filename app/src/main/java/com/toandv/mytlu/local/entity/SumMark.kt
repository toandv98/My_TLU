package com.toandv.mytlu.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sum_mark")
data class SumMark @JvmOverloads constructor(
    val nameHoc: String,
    val kyMotTL: String,
    val kyHaiTL: String,
    val caNamTL: String,
    val toanKhoa: String,
    var kyMotRL: String = "",
    var kyHaiRL: String = "",
    var caNamRL: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
