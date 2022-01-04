package ru.gb.kotlin.themoviedbapp.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.gb.kotlin.themoviedbapp.R
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.model.Movie
import ru.gb.kotlin.themoviedbapp.databinding.MainFragmentBinding
import ru.gb.kotlin.themoviedbapp.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

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
        binding.rvMainScreen.layoutManager = LinearLayoutManager(requireActivity())

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // Создали обсервер для подписки на LiveData
        val observer = Observer<AppState> { state ->
            renderData(state)
        }
        // Подписались на изменения LiveData
        viewModel.getData(isRussian).observe(viewLifecycleOwner, observer)

        // Запросили данные из LiveData
        viewModel.getData(isRussian)

        binding.fabMainChangeLanguage.setOnClickListener {
            isRussian = !isRussian

            if (isRussian) {
                binding.fabMainChangeLanguage.setImageResource(R.drawable.ic_russia)
            } else {
                binding.fabMainChangeLanguage.setImageResource(R.drawable.ic_baseline_flag_24)
            }
            viewModel.getData(isRussian)
        }

    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success<*> -> {
                binding.onLoadingContainer.visibility = View.GONE
                val movies: List<Movie> = state.data as List<Movie>
                adapter.setMovie(movies)
                adapter.listener = MainFragmentAdapter.OnItemClick { movie ->

                    val bundle = Bundle()
                    bundle.putParcelable("MOVIE_EXTRA", movie)

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main, DetailFragment.newInstance(bundle))
                        .addToBackStack("main")
                        .commit()
                }
            }
            is AppState.Error -> {
                binding.onLoadingContainer.visibility = View.VISIBLE
                Snackbar.make(binding.root, "Ошибка загрузки:\n ${state.error.message.toString()}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Повторить загрузку") {
                        viewModel.getData(isRussian)
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