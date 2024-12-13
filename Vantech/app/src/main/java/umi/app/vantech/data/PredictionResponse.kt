package umi.app.vantech.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictionResponse(
    val created_at: String,
    val diseaseRecords: List<DiseaseRecord>,
    val id: Int,
    val image: String,
    val user: User
) : Parcelable


