package br.com.alura.aluraesporte.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.aluraesporte.model.Usuario
import br.com.alura.aluraesporte.repository.FirebaseAuthRepository
import br.com.alura.aluraesporte.repository.Resource

class CadastroUsuarioViewModel(private val repository: FirebaseAuthRepository) : ViewModel(){

    fun cadastra(usuario: Usuario): LiveData<Resource<Boolean>> {
        return repository.cadastra(usuario)
    }

}