package umi.app.vantech.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid: String? = null,
    var username: String? = null,
    var password: String? = null,
    var namaLengkap: String? = null,
    var alamat: String? = null,
    var noTelp: String? = null,
    var email: String? = null,
): Parcelable {
    constructor() : this("", "", "", "","","","")
}
