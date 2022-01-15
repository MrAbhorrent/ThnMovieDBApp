package ru.gb.kotlin.themoviedbapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import ru.gb.kotlin.themoviedbapp.R
import ru.gb.kotlin.themoviedbapp.model.Movie

class MainFragmentAdapter: RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder>() {

    private var movies: List<Movie> = listOf()
    var listener: OnItemClick? = null

    fun setMovie(data: List<Movie>) {
        movies = data
        notifyDataSetChanged()
    }

    inner class MainFragmentViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            itemView.apply {
                movie.title.also { findViewById<TextView>(R.id.tvMovieTitle).text = it }
                movie.release_date.also { findViewById<TextView>(R.id.tvMovieRelease).text = it }
                movie.poster_path.also { findViewById<ImageView>(R.id.imgvPosterPreview).load("https://image.tmdb.org/t/p/w500${movie.poster_path}") {
                    crossfade(true)
                    placeholder(R.drawable.coming_soon)
                } }
                setOnClickListener {
                    listener?.onClick(movie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        return MainFragmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun interface OnItemClick {
        fun onClick(movie: Movie)
    }
}
