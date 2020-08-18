package br.com.alura.aluraesporte.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.aluraesporte.model.Usuario
import br.com.alura.aluraesporte.repository.FirebaseAuthRepository
import br.com.alura.aluraesporte.repository.Resource
import com.google.firebase.auth.AuthCredential

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    fun autentica(usuario: Usuario): LiveData<Resource<Boolean>> {
        return firebaseAuthRepository.autentica(usuario)
    }

    fun desloga() {
        firebaseAuthRepository.desloga()
    }

    fun estaLogado(): Boolean = firebaseAuthRepository.estaLogado()

    fun naoEstaLogado(): Boolean = !estaLogado()

    fun vinculaContaGoogle(credencial: AuthCredential): LiveData<Resource<Boolean>> =
        firebaseAuthRepository.vinculaContaGoogle(credencial)

}
