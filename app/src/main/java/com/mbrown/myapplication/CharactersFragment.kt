package com.mbrown.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val mAdapter = CharacterListAdapter {
        val characterId = it.id
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

        setupList()
        if(viewModel.characters.isNotEmpty()) {
            updateList()
        }
    }

    private fun getOrientation() : Int =
        activity?.resources?.configuration?.orientation ?: Configuration.ORIENTATION_UNDEFINED

    private fun setupList() {
        val columns = calculateColumns()
        val layout = if(getOrientation() == Configuration.ORIENTATION_LANDSCAPE && columns > 1) {
            GridLayoutManager(context, columns)
        }
        else {
            binding.characterList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            LinearLayoutManager(context)
        }

        binding.characterList.apply {
            adapter = mAdapter
            layoutManager = layout
        }

        binding.characterList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Check if the view is scrolled to the bottom
                if(!recyclerView.canScrollVertically(1)) {
                    Timber.d("Scrolled to bottom")
                    if(!viewModel.loading && viewModel.nextPage != null) {
                        fetchCharacters(viewModel.nextPage)
                    }
                }
            }
        })
    }

    private fun updateList() {
        mAdapter.items = viewModel.characters.map { CharacterItem(it.key, it.value.image, it.value.name, it.value.status, it.value.species) }
    }

    private fun calculateColumns(): Int {
        val columns = activity?.let { context ->
            val metrics =  context.resources.displayMetrics
            val viewWidth = (metrics.widthPixels / metrics.density) - 16 // Screen width - list margin
            val itemWidth = resources.getDimension(R.dimen.directory_list_item_width) / metrics.density
            (viewWidth / itemWidth).toInt()
        } ?: 1

        return if (columns > 0) columns else 1
    }

    override fun onStart() {
        super.onStart()
        subscriptions = CompositeDisposable()

        if(viewModel.characters.isEmpty()) {
            fetchCharacters()
        }
    }

    private fun fetchCharacters(page: Long? = null) {
        viewModel.loading = true
        subscriptions?.add(RickAndMortyService.rmService.getCharacters(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Timber.d(result.toString())

                viewModel.nextPage = result.info.next?.let { next ->
                    next.substring(next.lastIndexOf('=') + 1).toLongOrNull()
                }
                viewModel.characters.putAll(result.results.map { it.id to it }.toMap())
                viewModel.loading = false
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