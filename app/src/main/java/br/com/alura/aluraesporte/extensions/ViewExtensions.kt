package br.com.alura.aluraesporte.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(
    mensagem: String,
    duracao: Int = Snackbar.LENGTH_SHORT
){
    Snackbar.make(
        this,
        mensagem,
        duracao
    ).show()
}