package ru.gb.kotlin.themoviedbapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.kotlin.themoviedbapp.R
import ru.gb.kotlin.themoviedbapp.model.Movie

class MainFragmentAdapter: RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder>() {

    private var movie: List<Movie> = listOf()
    var listener: OnItemClick? = null

    fun setMovie(data: List<Movie>) {
        movie = data
        notifyDataSetChanged()
    }

    inner class MainFragmentViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.tvMovieTitle).text = movie.original_title
            itemView.setOnClickListener {
                listener?.onClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        return MainFragmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        holder.bind(movie[position])
    }

    override fun getItemCount(): Int = movie.size

    fun interface OnItemClick {
        fun onClick(movie: Movie)
    }
}
