package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.ProductModel
import com.zolfagharipour.market.databinding.ItemRowProductsInDetailBinding
import com.zolfagharipour.market.view.fragment.DetailFragmentDirections

class ProductsInDetailAdapter (
    private val viewModel: AndroidViewModel,
    val lifecycleOwner: LifecycleOwner,
    private val list: ArrayList<ProductModel>,
    val navController: NavController,
        ):
    RecyclerView.Adapter<ProductsInDetailAdapter.ProductsInDetailViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsInDetailViewHolder =
        ProductsInDetailViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewModel.getApplication()),
        R.layout.item_row_products_in_detail,
        parent,
        false))

    override fun onBindViewHolder(holder: ProductsInDetailViewHolder, position: Int) {
        holder.binding(list[position])
    }

    override fun getItemCount(): Int  = list.size

    inner class ProductsInDetailViewHolder(val binding: ItemRowProductsInDetailBinding) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.lifecycleOwner = lifecycleOwner
            }
        fun binding(product: ProductModel){
            binding.product = product
            binding.imageViewPhotoProduct.load(product.images[0])
            binding.root.setOnClickListener {
                navController.navigate(DetailFragmentDirections.actionDetailFragmentSelf(product))
            }
            binding.executePendingBindings()
        }
        }
}