package com.mbrown.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mbrown.myapplication.databinding.FragmentCharacterDetailBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class CharacterDetailFragment : Fragment() {

    private var subscriptions: CompositeDisposable? = null

    private val args: CharacterDetailFragmentArgs by navArgs()

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCharacterDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.characterName.text = args.name
        viewModel.location?.let { location ->
            updateLocationFields(location)
        }
    }

    private fun updateLocationFields(location: Model.DetailedLocation) {
        binding.planet.text = location.name
        binding.dimension.text = location.dimension
    }

    override fun onStart() {
        super.onStart()
        subscriptions = CompositeDisposable()

        if(viewModel.location == null) {
            fetchLocationData()
        }
    }

    private fun fetchLocationData() {
        subscriptions?.add(RickAndMortyService.rmService.getLocation(args.locationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Timber.d(result.toString())
                viewModel.location = result
                updateLocationFields(result)
            }, { error -> error.printStackTrace() })
        )
    }

    override fun onStop() {
        super.onStop()
        subscriptions?.dispose()
        subscriptions = null
    }
}