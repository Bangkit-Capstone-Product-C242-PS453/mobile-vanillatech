package umi.app.vantech.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiseaseRecord(
    val disease: Disease,
    val id: Int
) : Parcelable

