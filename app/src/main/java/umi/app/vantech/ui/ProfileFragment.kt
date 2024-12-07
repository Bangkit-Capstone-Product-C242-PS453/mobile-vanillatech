package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umi.app.vantech.Preferences
import umi.app.vantech.R
import umi.app.vantech.adapter.BannerAdapter
import umi.app.vantech.adapter.PlantAdapter
import umi.app.vantech.databinding.FragmentHomeBinding
import umi.app.vantech.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref : Preferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = Preferences(requireContext())

        binding.apply {
            tvKeluar.setOnClickListener {
                pref.clearPreferences()
                startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                requireActivity().finish()
            }
        }
    }


}