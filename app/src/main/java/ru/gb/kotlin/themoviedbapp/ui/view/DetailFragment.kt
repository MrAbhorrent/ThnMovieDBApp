package ru.gb.kotlin.themoviedbapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.R
import ru.gb.kotlin.themoviedbapp.databinding.DetailFragmentBinding
import ru.gb.kotlin.themoviedbapp.model.Movie
import ru.gb.kotlin.themoviedbapp.model.MovieIntentService
import ru.gb.kotlin.themoviedbapp.model.Repository
import ru.gb.kotlin.themoviedbapp.model.RepositoryImpl
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    var id: Int? = null

    companion object {
        fun newInstance(bundle: Bundle?): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    private val listener = Repository.OnLoadListener {

        RepositoryImpl.getMovieFromServer()?.let { movie ->
            with(binding) {
                tvMovieName.text = movie.title
                tvMovieYear.text = "Дата релиза: ${movie.release_date}"
                tvOriginalLanguage.text = "Язык - ${movie.original_language}"
                tvDescription.text = "Описание:\n ${movie.overview}"
                imgPosterPreview.load("https://image.tmdb.org/t/p/w500${movie.poster_path}") {
                    crossfade(true)
                    placeholder(R.drawable.coming_soon)
                }
            }
            Log.d("DEBUGLOG", "id - ${movie.id}; Release - ${movie.release_date}; Title - ${movie.original_title}")

        } ?: Toast.makeText(context, "ОШИБКА", Toast.LENGTH_LONG).show()
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RepositoryImpl.addLoadedListener(listener)
        arguments
            ?.getParcelable<Movie>(getString(R.string.KEY_MOVIE_EXTRA))
            ?.let { movie ->
                id = movie.id
                /*
                val observer = Observer<AppState> { state ->
                    renderData(state)
                }
                viewModel.getData().observe(viewLifecycleOwner, observer)
                viewModel.getMovie(movie.id)
               */
                // service + BroadcastReceiver
                requireActivity().startService(Intent(
                    requireContext(),
                    MovieIntentService::class.java
                ).apply {
                    putExtra(getString(R.string.KEY_MOVIE_EXTRA), movie)
                })

        } ?: snackbarMake(getString(R.string.txtErrorDataShowing))
    }

    private fun renderData(state: AppState?) {
        when (state) {
            is AppState.Success<*> -> {
                binding.onLoadingContainer.hide()
                val movie = state.data as Movie
                with(binding) {
                    tvMovieName.text = movie.title
                    tvMovieYear.text = "Дата релиза: ${movie.release_date}"
                    tvOriginalLanguage.text = "Язык - ${movie.original_language}"
                    tvDescription.text = "Описание:\n ${movie.overview}"
                    Log.d("DEBUGLOG", "Query image: https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    imgPosterPreview.load("https://image.tmdb.org/t/p/w500${movie.poster_path}") {
                        crossfade(true)
                        placeholder(R.drawable.coming_soon)
                    }
                }
            }
            is AppState.Error -> {
                binding.onLoadingContainer.show()
                Snackbar.make(binding.root, "Ошибка загрузки:\n ${state.error.message.toString()}", Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.messageRepeatLoading)) {
                        id?.let { it1 -> viewModel.getMovie(it1) }
                    }
                    .show()
            }
            is AppState.Loading ->
                binding.onLoadingContainer.show()
        }
    }

    private fun snackbarMake(s: String): Unit {
        Snackbar.make(binding.root, s, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        RepositoryImpl.removeLoadedListener(listener)
        _binding = null

    }
}