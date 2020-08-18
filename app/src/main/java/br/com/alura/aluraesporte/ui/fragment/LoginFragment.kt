package br.com.alura.aluraesporte.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.alura.aluraesporte.R
import br.com.alura.aluraesporte.extensions.googleSignInClient
import br.com.alura.aluraesporte.extensions.snackBar
import br.com.alura.aluraesporte.model.Usuario
import br.com.alura.aluraesporte.ui.viewmodel.ComponentesVisuais
import br.com.alura.aluraesporte.ui.viewmodel.EstadoAppViewModel
import br.com.alura.aluraesporte.ui.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

const val RC_SIGN_IN_GOOGLE = 1

class LoginFragment : Fragment() {

    private val controlador by lazy {
        findNavController()
    }
    private val viewModel: LoginViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.login,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        estadoAppViewModel.temComponentes = ComponentesVisuais()
        configuraBotaoLogin()
        configuraBotaoCadastro()
        login_botao_signin_google.setOnClickListener {
            val cliente = requireContext().googleSignInClient()
            startActivityForResult(cliente.signInIntent, RC_SIGN_IN_GOOGLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN_GOOGLE) {
            val contaGoogle = GoogleSignIn.getSignedInAccountFromIntent(data).result
            Log.i(TAG, "onActivityResult: conta google autenticada $contaGoogle")
            contaGoogle?.let { conta ->
                val credencial = GoogleAuthProvider.getCredential(conta.idToken, null)
                viewModel.vinculaContaGoogle(credencial)
                    .observe(viewLifecycleOwner, Observer {
                        it?.let { recurso ->
                            if(recurso.dado){
                                vaiParaListaProdutos()
                            } else {
                                val mensagem = recurso.erro ?: "Falha ao vincular com a conta Google"
                                view?.snackBar(mensagem)
                            }
                        }
                    })
            }
        }
    }

    private fun configuraBotaoCadastro() {
        login_botao_cadastrar_usuario.setOnClickListener {
            val direcao = LoginFragmentDirections
                .acaoLoginParaCadastroUsuario()
            controlador.navigate(direcao)
        }
    }

    private fun configuraBotaoLogin() {
        login_botao_logar.setOnClickListener {

            limpaCampos()

            val email = login_email.editText?.text.toString()
            val senha = login_senha.editText?.text.toString()

            if (validaCampos(email, senha)) {
                autentica(email, senha)
            }
        }
    }

    private fun autentica(email: String, senha: String) {
        viewModel.autentica(Usuario(email, senha))
            .observe(viewLifecycleOwner, Observer {
                it?.let { recurso ->
                    if (recurso.dado) {
                        vaiParaListaProdutos()
                    } else {
                        val mensagemErro = recurso.erro ?: "Erro durante a autenticação"
                        view?.snackBar(mensagemErro)
                    }
                }
            })
    }

    private fun validaCampos(email: String, senha: String): Boolean {
        var valido = true

        if (email.isBlank()) {
            login_email.error = "E-mail é obrigatório"
            valido = false
        }

        if (senha.isBlank()) {
            login_senha.error = "Senha é obrigatória"
            valido = false
        }
        return valido
    }

    private fun limpaCampos() {
        login_email.error = null
        login_senha.error = null
    }

    private fun vaiParaListaProdutos() {
        val direcao = LoginFragmentDirections.acaoLoginParaListaProdutos()
        controlador.navigate(direcao)
    }

}