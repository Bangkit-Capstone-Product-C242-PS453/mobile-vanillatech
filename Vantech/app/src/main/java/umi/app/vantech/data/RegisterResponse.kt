package umi.app.vantech.data

data class RegisterResponse(
    val status: String,         // Status registrasi, misalnya "success"
    val message: String?,       // Pesan tambahan dari server
    val userId: Int?            // ID pengguna yang baru terdaftar (opsional)
)
