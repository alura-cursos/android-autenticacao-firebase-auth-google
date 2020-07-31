package br.com.alura.aluraesporte.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alura.aluraesporte.model.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import java.lang.Exception
import java.lang.IllegalArgumentException

private const val TAG = "FirebaseAuthRepository"

class FirebaseAuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun desloga() {
        firebaseAuth.signOut()
    }

    fun cadastra(usuario: Usuario): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        try {
            val tarefa =
                firebaseAuth.createUserWithEmailAndPassword(usuario.email, usuario.senha)
            tarefa.addOnSuccessListener {
                Log.i(TAG, "cadastra: cadastro sucedido")
                liveData.value = Resource(true)
            }
            tarefa.addOnFailureListener { exception ->
                Log.e(TAG, "cadastra: cadastro falhou", exception)
                val mensagemErro: String = devolveErroDeCadastro(exception)
                liveData.value = Resource(false, mensagemErro)
            }
        } catch (e: IllegalArgumentException) {
            liveData.value = Resource(false, "E-mail ou senha não ser vazio")
        }
        return liveData
    }

    private fun devolveErroDeCadastro(exception: Exception): String = when (exception) {
        is FirebaseAuthWeakPasswordException -> "Senha precisa de pelo menos 6 dígitos"
        is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado"
        else -> "Erro desconhecido"
    }

    fun estaLogado(): Boolean {
        val usuarioFirebase: FirebaseUser? = firebaseAuth.currentUser
        if (usuarioFirebase != null) {
            return true
        }
        return false
    }

    fun autentica(usuario: Usuario): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        try {
            firebaseAuth.signInWithEmailAndPassword(usuario.email, usuario.senha)
                .addOnCompleteListener { tarefa ->
                    if (tarefa.isSuccessful) {
                        liveData.value = Resource(true)
                    } else {
                        Log.e(TAG, "autentica: ", tarefa.exception)
                        val mensagemErro: String = devolveErroDeAutenticao(tarefa.exception)
                        liveData.value = Resource(false, mensagemErro)
                    }
                }
        } catch (e: IllegalArgumentException) {
            liveData.value = Resource(false, "E-mail ou senha não pode ser vazio")
        }
        return liveData
    }

    private fun devolveErroDeAutenticao(exception: Exception?): String = when (exception) {
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos"
        else -> "Erro desconhecido"
    }

    fun usuario(): LiveData<Usuario> {
        val liveData = MutableLiveData<Usuario>()
        firebaseAuth.currentUser?.let {firebaseUser ->
            firebaseUser.email?.let {email ->
                liveData.value = Usuario(email)
            }
        }
        return liveData
    }

}