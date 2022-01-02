package ru.gb.kotlin.themoviedbapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.model.Movie
import ru.gb.kotlin.themoviedbapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Создали обсервер для подписки на LiveData
        val observer = Observer<AppState> { state ->
            renderData(state)
        }
        // Подписались на изменения LiveData
        viewModel.getData().observe(viewLifecycleOwner, observer)
        // Запросили данные из LiveData
        viewModel.getData()
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        val observer = Observer<Any> {
//            renderData(it)
//        }
//        viewModel.getData().observe(viewLifecycleOwner, observer)
//    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                binding.onLoadingContainer.visibility = View.GONE
                val movie = state.movie as Movie
                binding.tvMovieName.text = movie.nameMovie
                binding.tvMovieYear.text = movie.yearMovie.toString()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}