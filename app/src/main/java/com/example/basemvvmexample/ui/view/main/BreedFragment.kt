package com.example.basemvvmexample.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basemvvmexample.R
import com.example.basemvvmexample.data.api.response.DogBreedResponse
import com.example.basemvvmexample.data.api.response.Resource
import com.example.basemvvmexample.data.api.response.Status
import com.example.basemvvmexample.databinding.BreedFragmentBinding
import com.example.basemvvmexample.ui.BreedRecyclerViewAdapter
import com.example.basemvvmexample.ui.viewmodel.BreedViewModel
import com.example.basemvvmexample.ui.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.breed_fragment.breed_fragment_button
import kotlinx.android.synthetic.main.breed_fragment.breed_fragment_recycler_view
import kotlinx.android.synthetic.main.breed_fragment.breed_fragment_search
import retrofit2.Response

class BreedFragment : Fragment() {

    // private lateinit var viewModel: BreedViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var breedFragmentBinding: BreedFragmentBinding
    private lateinit var getDogBreedsObserverResponse: Observer<Resource<Response<DogBreedResponse>>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        breedFragmentBinding = BreedFragmentBinding.inflate(inflater, container, false).apply {
            breedViewModel = ViewModelProvider(this@BreedFragment).get(BreedViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }

        return breedFragmentBinding.root
        // return inflater.inflate(R.layout.breed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        // initializeUI()

        breed_fragment_button.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_breedFragment_to_subBreedFragment)
        }

        breed_fragment_search.setOnClickListener {
            breedFragmentBinding.breedViewModel?.getDogBreeds()?.observe(viewLifecycleOwner, getDogBreedsObserverResponse)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        }
    }

    private fun initRecyclerView(items: List<String>) {
        val recyclerViewAdapter = BreedRecyclerViewAdapter(items, sharedViewModel)
        breed_fragment_recycler_view.adapter = recyclerViewAdapter
        breed_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    private fun initializeUI() {
        breedFragmentBinding.breedViewModel?.getDogBreeds()?.observe(viewLifecycleOwner, getDogBreedsObserverResponse)
    }

    private fun setUpObservers() {
        getDogBreedsObserverResponse = Observer<Resource<Response<DogBreedResponse>>> {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val list = resource.data?.body()?.message?.keys?.toList()
                        // breedFragmentBinding.breedViewModel?.saveThatShit(list)
                        list?.let { it1 -> initRecyclerView(it1) }
                    }
                    Status.ERROR -> {
                        // Do something if error
                    }
                    Status.LOADING -> {
                        // Do something if loading
                    }
                }
            }
        }
    }
}
