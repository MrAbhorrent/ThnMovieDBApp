package ru.gb.kotlin.themoviedbapp.ui.view

import android.view.View

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}