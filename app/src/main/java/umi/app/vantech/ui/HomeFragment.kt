package umi.app.vantech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umi.app.vantech.Preferences
import umi.app.vantech.R
import umi.app.vantech.adapter.BannerAdapter
import umi.app.vantech.adapter.PlantAdapter
import umi.app.vantech.data.Banner
import umi.app.vantech.data.Plant
import umi.app.vantech.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var pref : Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        pref = Preferences(requireContext())
//        val username = pref.getValues("USERNAME")
//
//        binding.tvHello.setText("Hello, $username\nHave a nice day!")

        binding.rvSlidebar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvSlidebarTanaman.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Prepare banner data
        val bannerImages = resources.obtainTypedArray(R.array.data_banner)
        val banners = mutableListOf<Banner>()
        for (i in 0 until bannerImages.length()) {
            banners.add(Banner(bannerImages.getResourceId(i, -1)))
        }
        bannerImages.recycle()

        // Prepare plant data
        val plantImages = resources.obtainTypedArray(R.array.data_foto_tanaman)
        val plants = mutableListOf<Plant>()
        for (i in 0 until plantImages.length()) {
            plants.add(Plant(plantImages.getResourceId(i, -1)))
        }
        plantImages.recycle()

        // Set adapters
        bannerAdapter = BannerAdapter(banners)
        binding.rvSlidebar.adapter = bannerAdapter

        plantAdapter = PlantAdapter(plants)
        binding.rvSlidebarTanaman.adapter = plantAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}