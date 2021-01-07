package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.data.room.entities.Product
import com.zolfagharipour.market.databinding.ItemRowProductsBinding
import com.zolfagharipour.market.enums.FragmentHostEnum
import com.zolfagharipour.market.view.fragment.CategoryFragmentDirections
import com.zolfagharipour.market.view.fragment.DetailFragmentDirections
import com.zolfagharipour.market.view.fragment.HomeFragmentDirections

class ProductsAdapter(private val viewModel: AndroidViewModel, val lifecycleOwner: LifecycleOwner, val productList: ArrayList<Product>, val navController: NavController, val fragmentHostEnum: FragmentHostEnum) :
    RecyclerView.Adapter<ProductsAdapter.ProductsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
       val binding: ItemRowProductsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewModel.getApplication()),
                R.layout.item_row_products, parent, false
            )
            return ProductsHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.bindProducts(productList[position])
    }


    override fun getItemCount(): Int = productList.size

    inner class ProductsHolder(var binding: ItemRowProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lifecycleOwner = lifecycleOwner

        }

        fun bindProducts(product: Product) {
            binding.product = product
            binding.imageViewPhotoProduct.load(product.images[0])
            binding.cardViewContainerItemRowProduct.setOnClickListener {
                var action: NavDirections? = null
                if (fragmentHostEnum == FragmentHostEnum.HOME)
                    action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(product)
                else if (fragmentHostEnum == FragmentHostEnum.CATEGORY)
                    action = CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(product)
                else if (fragmentHostEnum == FragmentHostEnum.DETAIL)
                    action = DetailFragmentDirections.actionDetailFragmentSelf(product)
                navController.navigate(action!!)
            }
            binding.executePendingBindings()
        }
    }
}