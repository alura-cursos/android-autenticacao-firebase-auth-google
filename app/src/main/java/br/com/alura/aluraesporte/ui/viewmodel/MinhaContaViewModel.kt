package br.com.alura.aluraesporte.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.aluraesporte.model.Usuario
import br.com.alura.aluraesporte.repository.FirebaseAuthRepository

class MinhaContaViewModel(repository: FirebaseAuthRepository) : ViewModel() {

    val usuario: LiveData<Usuario> = repository.usuario()

}