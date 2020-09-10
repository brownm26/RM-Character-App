package com.mbrown.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbrown.myapplication.databinding.FragmentCharactersBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class CharactersFragment : Fragment() {

    private var subscriptions: CompositeDisposable? = null

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    private val mAdapter = TextListAdapter {
        val characterId = (it as TextIdItem).id
        Timber.d("Character id: $id")

        val character = viewModel.characters[characterId]
        character?.location?.url?.let { url ->
            val path = url.path
            path.substring(path.lastIndexOf('/') + 1).toLongOrNull()?.let { id ->
                val action = CharactersFragmentDirections.actionShowCharacterDetails(character.name, id)
                findNavController().navigate(action)
            } ?: Timber.e("Unable to parse url: $url")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCharactersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.characterList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        if(viewModel.characters.isNotEmpty()) {
            updateList()
        }
    }

    private fun updateList() {
        mAdapter.items = viewModel.characters.map { TextIdItem(it.key, it.value.name) }
    }

    override fun onStart() {
        super.onStart()
        subscriptions = CompositeDisposable()

        if(viewModel.characters.isEmpty()) {
            fetchCharacters()
        }
    }

    private fun fetchCharacters() {
        subscriptions?.add(RickAndMortyService.rmService.getCharacters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Timber.d(result.toString())
                viewModel.characters.putAll(result.results.map { it.id to it }.toMap())
                updateList()
            }, { error -> error.printStackTrace() })
        )
    }

    override fun onStop() {
        super.onStop()
        subscriptions?.dispose()
        subscriptions = null
    }
}