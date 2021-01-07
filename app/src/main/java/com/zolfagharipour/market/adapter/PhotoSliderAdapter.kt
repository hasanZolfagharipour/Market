package com.zolfagharipour.market.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zolfagharipour.market.R
import com.zolfagharipour.market.databinding.ItemRowPhotoDetailProductBinding
import com.zolfagharipour.market.viewModel.DetailViewModel

class PhotoSliderAdapter(val viewModel: DetailViewModel, val lifecycleOwner: LifecycleOwner, val images: ArrayList<String>): RecyclerView.Adapter<PhotoSliderAdapter.PhotoSliderViewHolder>() {

    inner class PhotoSliderViewHolder(val binding: ItemRowPhotoDetailProductBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        fun bindImageSlider(url: String){
            binding.imageViewPhotoItemRow.load(url)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSliderViewHolder {
        return PhotoSliderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewModel.getApplication()), R.layout.item_row_photo_detail_product, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoSliderViewHolder, position: Int) {
        holder.bindImageSlider(images[position])
    }

    override fun getItemCount(): Int = images.size
}