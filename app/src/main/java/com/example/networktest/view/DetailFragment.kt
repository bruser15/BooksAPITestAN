package com.example.networktest.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.networktest.R
import com.example.networktest.databinding.DetailFragmentLayoutBinding
import com.example.networktest.model.presentation.VolumeItem
import com.squareup.picasso.Picasso

private const val TAG = "DetailFragment"
class DetailFragment: Fragment() {

    companion object{
        const val DETAIL_BOOK = "DetailBook"
        fun newInstance(book: VolumeItem): DetailFragment{
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DETAIL_BOOK, book)
                }
            }
        }
    }

    private lateinit var binding: DetailFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        binding = DetailFragmentLayoutBinding.inflate(
            inflater,
            container,
            false)

        arguments?.getParcelable<VolumeItem>(DETAIL_BOOK)?.let {
            initViews(it)
        }

        return binding.root
    }

    private fun initViews(dataSet: VolumeItem) {
        binding.tvBookAuthors.text = binding.root.context.getString(
            R.string.book_authors, dataSet.authors.toString())
        binding.tvBookTitle.text = binding.root.context.getString(
            R.string.book_title, dataSet.title)
        binding.tvBookDescription.text = dataSet.description

        Picasso.get()
            .load(dataSet.imageLinks?.thumbnail)
            .into(binding.ivBookCover)
        //Log.d(TAG, "initViews: ${dataSet.imageLinks?.thumbnail}")
    }
}