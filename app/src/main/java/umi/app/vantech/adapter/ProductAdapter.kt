//package umi.app.vantech.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import umi.app.vantech.data.Product
//import umi.app.vantech.databinding.ItemProductBinding
//
//class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
//
//    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ProductViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = products[position]
//        with(holder.binding) {
//            imgBerita.setImageResource(product.imageResId)
//            tvNamaProduk.text = product.name
//            tvHarga.text = product.price
//        }
//    }
//
//    override fun getItemCount(): Int = products.size
//}