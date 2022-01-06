package ru.gb.kotlin.themoviedbapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.databinding.DetailFragmentBinding
import ru.gb.kotlin.themoviedbapp.model.Movie
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

        arguments
            ?.getParcelable<Movie>("MOVIE_EXTRA")
            ?.let { movie ->
                id = movie.id
                val observer = Observer<AppState> { state ->
                    renderData(state)
                }
                viewModel.getData(movie.id).observe(viewLifecycleOwner, observer)
                viewModel.getData(movie.id)

        } ?: snackbarMake("Ошибка отображения данных")
    }

    private fun renderData(state: AppState?) {
        when (state) {
            is AppState.Success<*> -> {
                binding.onLoadingContainer.hide()
                val movie = state.data as Movie
                binding.tvMovieName.text = movie.original_title
                binding.tvMovieYear.text = movie.release_date
                binding.tvOriginalLanguage.text = movie.original_language
                binding.tvDescription.text = movie.overview
            }
            is AppState.Error -> {
                binding.onLoadingContainer.show()
                Snackbar.make(binding.root, "Ошибка загрузки:\n ${state.error.message.toString()}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Повторить загрузку") {
                        id?.let { it1 -> viewModel.getData(it1) }
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
        _binding = null
    }
}