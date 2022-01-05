package ru.gb.kotlin.themoviedbapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.databinding.DetailFragmentBinding
import ru.gb.kotlin.themoviedbapp.model.Movie
import ru.gb.kotlin.themoviedbapp.viewmodel.DetailViewModel

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

        arguments
            ?.getParcelable<Movie>("MOVIE_EXTRA")
            ?.let { movie ->
            binding.tvMovieName.text = movie.original_title
            binding.tvMovieYear.text = movie.release_date
            binding.tvGenres.text = movie.genres.name
        } ?: snackbarMake("Ошибка отображения данных")
    }

    private fun snackbarMake(s: String): Unit {
        Snackbar.make(binding.root, s, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}