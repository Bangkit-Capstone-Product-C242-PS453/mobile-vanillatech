package umi.app.vantech.data


data class PenyakitVanilla(
    val namaAlias: String,
    val namaPenyakit: String,
    val namaLain: String,
    val gejala: String,
    val solusi: String
)

object PenyakitRepository {
    val diseases = listOf(
        PenyakitVanilla("Scab","Akar Busuk","(Root Rot)","•\tDaun tanaman menjadi kuning, layu, dan rontok.\n•\tTanaman menjadi kerdil dan pertumbuhannya terhenti.\n•\tAkar menjadi coklat, lembek, dan membusuk.\n•\tBiasanya disebabkan oleh jamur patogen seperti Fusarium sp. atau Pythium sp. yang menyerang akar.", "•\tJaga kelembapan tanah agar tidak terlalu basah, terutama di daerah yang memiliki drainase buruk.\n•\tGunakan fungisida sistemik yang mengandung bahan aktif seperti metalaksil atau fosetil-aluminium.\n•\tPastikan sanitasi lahan baik, serta hindari genangan air di sekitar tanaman.\n•\tLakukan pergiliran tanaman dan gunakan bibit yang sehat." ),
        PenyakitVanilla("Rust", "Busuk Batang", "(Stem Rot)", "•\tBatang mulai mengalami pembusukan dari pangkal dan meluas ke seluruh bagian tanaman.\n•\tBatang berubah warna menjadi coklat kehitaman dan berbau tidak sedap.\n•\tTanaman menjadi layu dan akhirnya mati.\n•\tDisebabkan oleh jamur Fusarium oxysporum atau Phytophthora sp.","•\tPotong dan musnahkan bagian tanaman yang terinfeksi.\n•\tTingkatkan aerasi tanah dan hindari kelembapan yang berlebihan.\n•\tTerapkan fungisida yang efektif seperti fosetil-aluminium dan metalaksil.\n•\tGunakan metode perbanyakan tanaman dari bagian yang sehat."),
        PenyakitVanilla("Healty","Busuk Daun", "(Leaf Rot)", "•\tTerdapat bercak-bercak coklat atau hitam pada daun yang bisa meluas dan menyebabkan daun membusuk.\n•\tDaun mulai mengering dan rontok.\n•\tPenyebab umum adalah Colletotrichum sp. atau Phytophthora sp.","•\tPangkas daun yang terinfeksi dan bakar untuk mencegah penyebaran.\n•\tGunakan fungisida berbahan aktif seperti tembaga oksiklorida atau mankozeb.\n•\tGunakan fungisida berbahan aktif seperti tembaga oksiklorida atau mankozeb."),
        PenyakitVanilla("Black Rot","Hawar Daun dan Bunga", "(Leaf and Flower Blight)","•\tMuncul bercak-bercak coklat atau abu-abu pada daun, bunga, dan buah.\n•\tBunga atau buah muda akan gugur sebelum matang.\n•\tBercak tersebut dapat meluas hingga menyebabkan seluruh daun atau bunga membusuk.•\tBiasanya disebabkan oleh jamur Phytophthora sp. atau Colletotrichum sp.","•\tLakukan sanitasi kebun secara rutin, terutama membuang bagian tanaman yang terinfeksi.\n•\tLakukan sanitasi kebun secara rutin, terutama membuang bagian tanaman yang terinfeksi.\n•\tJaga kebersihan lingkungan kebun dan pastikan sirkulasi udara baik."),
        PenyakitVanilla("Common Rust","Karat Daun", "(Leaf Rust)", "•\tDaun menunjukkan adanya bercak kuning atau oranye pada permukaan bawah daun.\n•\tBercak ini seringkali berkembang menjadi pustula yang berisi spora jamur.\n•\tPada infeksi berat, daun menjadi kuning dan gugur.\n•\tPenyebab utamanya adalah jamur Hemileia vastatrix.","•\tPangkas dan bakar daun yang terinfeksi untuk mencegah penyebaran lebih lanjut.\n•\tTerapkan fungisida berbahan aktif seperti sulfur atau tembaga oksiklorida.\n•\tGunakan varietas vanili yang tahan terhadap penyakit karat daun jika tersedia."),
        PenyakitVanilla("Powdery Mildew","Powder Mildew", "(Embun Tepung)","•\tGunakan varietas vanili yang tahan terhadap penyakit karat daun jika tersedia.\n•\tDaun yang terkena infeksi bisa menjadi keriting, kering, dan rontok.\n•\tJika dibiarkan, dapat menghambat pertumbuhan tanaman secara signifikan.\n•\tDisebabkan oleh jamur Oidium sp. atau Erysiphe sp.","•\tPotong dan buang daun yang terinfeksi.\n•\tGunakan fungisida seperti sulfur atau fungisida berbasis minyak neem yang ramah lingkungan.\n•\tJaga kebersihan tanaman dan hindari kelembapan yang berlebihan di sekitar tanaman.")
    )

    fun getDetailPenyakitByName(namaPenyakit: String): PenyakitVanilla? {
        return diseases.find { it.namaAlias == namaPenyakit }
    }


}