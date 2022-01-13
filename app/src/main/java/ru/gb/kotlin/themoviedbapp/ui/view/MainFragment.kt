package ru.gb.kotlin.themoviedbapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.R
import ru.gb.kotlin.themoviedbapp.databinding.MainFragmentBinding
import ru.gb.kotlin.themoviedbapp.model.Movie
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = MainFragmentAdapter()
    private var isRussian: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMainScreen.adapter = adapter
        binding.rvMainScreen.layoutManager = GridLayoutManager(requireActivity(),2)

        // Создали наблюдатель для подписки на LiveData
        val observer = Observer<AppState> { state ->
            renderData(state)
        }
        // Подписались на изменения LiveData
        viewModel.getData().observe(viewLifecycleOwner, observer)

        // Запросили данные из LiveData
        viewModel.getMovies(isRussian)

        binding.fabMainChangeLanguage.setOnClickListener {
            isRussian = !isRussian

            when (isRussian) {
                true -> binding.fabMainChangeLanguage.setImageResource(R.drawable.ic_russia)
                false -> binding.fabMainChangeLanguage.setImageResource(R.drawable.ic_baseline_flag_24)
            }
            viewModel.getData()
        }
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success<*> -> {
                binding.onLoadingContainer.hide()
                val movies = state.data as List<Movie>
                adapter.setMovie(movies)
                adapter.listener = MainFragmentAdapter.OnItemClick { movie ->

                    val bundle = Bundle().apply {
                        putParcelable(getString(R.string.KEY_MOVIE_EXTRA), movie)
                    }
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main, DetailFragment.newInstance(bundle))
                        .addToBackStack("main")
                        .commit()
                }
            }
            is AppState.Error -> {
                binding.onLoadingContainer.show()
                Snackbar.make(binding.root, "Ошибка загрузки:\n ${state.error.message.toString()}", Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.messageRepeatLoading)) {
                        viewModel.getMovies(isRussian)
                    }
                    .show()
            }
            is AppState.Loading ->
                binding.onLoadingContainer.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}