package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.databinding.ItemRowLabelBinding
import com.zolfagharipour.market.databinding.ItemRowLastProductsBinding
import com.zolfagharipour.market.viewModel.HomeViewModel

class ProductsAdapter(private val homeViewModel: HomeViewModel, val lifecycleOwner: LifecycleOwner, val productList: ArrayList<Product>) :
    RecyclerView.Adapter<ProductsAdapter.ProductsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
       val binding: ItemRowLastProductsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(homeViewModel.getApplication()),
                R.layout.item_row_last_products, parent, false
            )
            return ProductsHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.bindProducts(productList[position])
    }


    override fun getItemCount(): Int = homeViewModel.lastProducts.value!!.size

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