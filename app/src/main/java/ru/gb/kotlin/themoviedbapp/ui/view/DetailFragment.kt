package ru.gb.kotlin.themoviedbapp.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.databinding.DetailFragmentBinding
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.model.Movie
import ru.gb.kotlin.themoviedbapp.databinding.MainFragmentBinding
import ru.gb.kotlin.themoviedbapp.viewmodel.DetailViewModel
import ru.gb.kotlin.themoviedbapp.viewmodel.MainViewModel

class DetailFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle?): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: DetailViewModel

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

        val movie = arguments?.getParcelable<Movie>("MOVIE_EXTRA")

        binding.tvMovieName.text = movie?.original_title ?: "-"
        binding.tvMovieYear.text = movie?.release_date ?: "-"
        binding.tvGenres.text = movie?.genres?.name ?: "-"
//        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
//
//        // Создали обсервер для подписки на LiveData
//        val observer = Observer<AppState> { state ->
//            renderData(state)
//        }
//        // Подписались на изменения LiveData
//        viewModel.getData().observe(viewLifecycleOwner, observer)
//        // Запросили данные из LiveData
//        viewModel.getData()
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success<*> -> {
                binding.onLoadingContainer.visibility = View.GONE
                val movie = state.data as Movie
                binding.tvMovieName.text = movie.original_title
                binding.tvMovieYear.text = movie.release_date
                binding.tvGenres.text = movie.genres.name
            }
            is AppState.Error -> {
                binding.onLoadingContainer.visibility = View.VISIBLE
                Snackbar.make(binding.root, "Ошибка загрузки:\n ${state.error.message.toString()}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Повторить загрузку") {
                        viewModel.getData()
                    }
                    .show()
            }
            is AppState.Loading ->
                binding.onLoadingContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}