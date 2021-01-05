package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.databinding.ItemRowLastProductsBinding

class ProductsAdapter(private val viewModel: AndroidViewModel, val lifecycleOwner: LifecycleOwner, val productList: ArrayList<Product>) :
    RecyclerView.Adapter<ProductsAdapter.ProductsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
       val binding: ItemRowLastProductsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_last_products, parent, false
            )
            return ProductsHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.bindProducts(productList[position])
    }


    override fun getItemCount(): Int = productList.size

    inner class ProductsHolder(var binding: ItemRowLastProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindProducts(product: Product) {
            binding.product = product
            binding.imageViewPhotoProduct.load(product.images[0])
            binding.executePendingBindings()
        }
    }
}