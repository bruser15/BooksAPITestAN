package com.example.networktest.view

import androidx.recyclerview.widget.RecyclerView
import com.example.networktest.R
import com.example.networktest.databinding.ItemLayoutBinding
import com.example.networktest.model.presentation.VolumeItem
import com.squareup.picasso.Picasso

class BookViewHolder(private val binding: ItemLayoutBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun onBind(volume: VolumeItem, callback: (VolumeItem) -> Unit){
            // String templates
            // Title: " "
            // Authors: " , "
            binding.tvBookAuthors.text = binding.root.context.getString(
                    R.string.book_authors, volume.authors.toString())
            binding.tvBookTitle.text = binding.root.context.getString(
                R.string.book_title, volume.title)

            Picasso.get()
                .load(volume.imageLinks?.thumbnail)
                .into(binding.ivBookCover)

            binding.root.setOnClickListener{
                callback(volume)
            }
        }
}