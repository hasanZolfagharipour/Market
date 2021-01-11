package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.databinding.ItemRowProductInCategoryBinding
import com.zolfagharipour.market.view.fragment.ProductsCategoryFragmentDirections
import com.zolfagharipour.market.viewModel.ProductsCategoryViewModel

class ProductsInCategoryAdapter(
    val viewModel: ProductsCategoryViewModel,
    val lifecycleOwner: LifecycleOwner,
    private val list: ArrayList<ProductModel>,
    val navController: NavController
) :
    RecyclerView.Adapter<ProductsInCategoryAdapter.ProductsInCategoryHolder>() {

    inner class ProductsInCategoryHolder(val binding: ItemRowProductInCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindingItems(productModel: ProductModel) {

            binding.imageViewPhoto.load(productModel.images[0])
            binding.product = productModel
            binding.root.setOnClickListener {
                navController.navigate(
                    ProductsCategoryFragmentDirections.actionProductsCategoryFragmentToDetailFragment(
                        productModel
                    )
                )
            }
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsInCategoryHolder =
        ProductsInCategoryHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_product_in_category,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductsInCategoryHolder, position: Int) {
        holder.bindingItems(list[position])
    }

    override fun getItemCount(): Int = list.size
}