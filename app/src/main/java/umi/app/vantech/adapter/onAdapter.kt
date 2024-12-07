package umi.app.vantech.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umi.app.vantech.R
import umi.app.vantech.ui.LoginActivity
import umi.app.vantech.ui.RegisterActivity

class OnAdapter(
    private val onNextClicked: () -> Unit
) : RecyclerView.Adapter<OnAdapter.OnboardingViewHolder>() {

    private val layouts = listOf(
        R.layout.onboarding1,
        R.layout.onboarding2,
        R.layout.onboarding3,
        R.layout.onboarding4
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return OnboardingViewHolder(view, viewType, onNextClicked)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        // Tidak diperlukan bind khusus karena semua ada di ViewHolder
    }

    override fun getItemCount() = layouts.size

    override fun getItemViewType(position: Int) = layouts[position]

    inner class OnboardingViewHolder(
        view: View,
        private val viewType: Int,
        private val onNextClicked: () -> Unit
    ) : RecyclerView.ViewHolder(view) {

        init {
            if (viewType == R.layout.onboarding4) {
                setupLoginRegisterButtons(view)
            } else {
                itemView.setOnClickListener {
                    onNextClicked()
                }
            }
        }

        private fun setupLoginRegisterButtons(view: View) {
            val btnLogin = view.findViewById<View>(R.id.btn_login)
            val btnRegister = view.findViewById<View>(R.id.btn_register)

            btnLogin?.setOnClickListener {
                val context = it.context
                context.startActivity(Intent(context, LoginActivity::class.java))
            }

            btnRegister?.setOnClickListener {
                val context = it.context
                context.startActivity(Intent(context, RegisterActivity::class.java))
            }
        }
    }
}
