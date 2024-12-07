package umi.app.vantech.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import umi.app.vantech.R
import umi.app.vantech.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            replaceFragment(getFragment(item.itemId))
            true
        }

        updateIconSize(binding.bottomNavigation.menu.findItem(R.id.nav_home))
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    private fun getFragment(itemId: Int): Fragment {
        return when (itemId) {
            R.id.nav_home -> HomeFragment().also { updateIconSize(binding.bottomNavigation.menu.findItem(
                R.id.nav_home
            )) }
//            R.id.nav_keranjang -> StoreFragment().also { updateIconSize(binding.bottomNavigation.menu.findItem(
//                R.id.nav_keranjang
//            )) }
            R.id.nav_scan -> ScanFragment().also { updateIconSize(binding.bottomNavigation.menu.findItem(
                R.id.nav_scan
            )) }
//            R.id.nav_konsul -> KonsulFragment().also { updateIconSize(binding.bottomNavigation.menu.findItem(
//                R.id.nav_konsul
//            )) }
            R.id.nav_profil -> ProfileFragment().also { updateIconSize(binding.bottomNavigation.menu.findItem(
                R.id.nav_profil
            )) }
            else -> HomeFragment()
        }
    }

    private fun updateIconSize(selectedItem: MenuItem) {
        for (i in 0 until binding.bottomNavigation.menu.size()) {
            val item = binding.bottomNavigation.menu.getItem(i)
            applyScaleAnimation(binding.bottomNavigation.findViewById(item.itemId), 1.0f) // Ukuran normal
        }


        applyScaleAnimation(binding.bottomNavigation.findViewById(selectedItem.itemId), 1.7f) // Ukuran besar saat dipilih
    }

    private fun applyScaleAnimation(view: View, scaleTo: Float) {
        val scaleAnimation = ScaleAnimation(
            1.0f, scaleTo,
            1.0f, scaleTo,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = 200
        scaleAnimation.fillAfter = true
        view.startAnimation(scaleAnimation)
    }
}