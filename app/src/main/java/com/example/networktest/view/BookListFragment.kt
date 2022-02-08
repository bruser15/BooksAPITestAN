package com.example.networktest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.networktest.R
import com.example.networktest.databinding.BookDisplayFragmentLayoutBinding
import com.example.networktest.model.presentation.BookResponse
import com.example.networktest.model.presentation.VolumeItem

class BookListFragment: Fragment() {

    companion object{
        const val BOOK_RESPONSE_DATA = "BookResponseData"

        fun newInstance(bookResponse: BookResponse): BookListFragment{
            val fragment = BookListFragment()
            val bundle = Bundle()
            bundle.putParcelable(BOOK_RESPONSE_DATA, bookResponse)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var binding: BookDisplayFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BookDisplayFragmentLayoutBinding.inflate(
            inflater,
            container,
            false)

        arguments?.let { bundle->
            bundle.getParcelable<BookResponse>(BOOK_RESPONSE_DATA)?.let {
                initViews(it)
            }
        }

        return binding.root
    }

    private fun initViews(dataSet: BookResponse) {
        binding.bookListResult.layoutManager = GridLayoutManager(context, 2)
        binding.bookListResult.adapter = BookAdapter(dataSet.items.map {
            VolumeItem(it.volumeInfo.title, it.volumeInfo.authors, it.volumeInfo.imageLinks, it.volumeInfo.description)
        }){
            requireActivity().openDisplayFragment(it)
        }
    }

    private fun FragmentActivity.openDisplayFragment(book: VolumeItem){
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, DetailFragment.newInstance(book))
            //.setTransition() // custom opening animation
            .addToBackStack(null) // allow back button to destroy fragment
            .commit()
    }
}