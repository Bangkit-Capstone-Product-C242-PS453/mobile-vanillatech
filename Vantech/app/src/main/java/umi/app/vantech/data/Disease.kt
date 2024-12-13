package umi.app.vantech.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Disease(
    val id: Int,
    val name: String,
    val prevention: List<String>,
    val symptoms: List<String>
) : Parcelable