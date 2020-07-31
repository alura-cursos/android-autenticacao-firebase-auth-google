package br.com.alura.aluraesporte.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.alura.aluraesporte.R
import br.com.alura.aluraesporte.extensions.snackBar
import br.com.alura.aluraesporte.model.Usuario
import br.com.alura.aluraesporte.ui.viewmodel.CadastroUsuarioViewModel
import br.com.alura.aluraesporte.ui.viewmodel.ComponentesVisuais
import br.com.alura.aluraesporte.ui.viewmodel.EstadoAppViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.cadastro_usuario.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CadastroUsuarioFragment : Fragment() {

    private val controlador by lazy {
        findNavController()
    }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val viewModel: CadastroUsuarioViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.cadastro_usuario,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        estadoAppViewModel.temComponentes = ComponentesVisuais()
        configuraBotaoCadastro()
    }

    private fun configuraBotaoCadastro() {
        cadastro_usuario_botao_cadastrar.setOnClickListener {

            limpaTodosCampos()

            val email = cadastro_usuario_email.editText?.text.toString()
            val senha = cadastro_usuario_senha.editText?.text.toString()
            val confirmaSenha = cadastro_usuario_confirma_senha.editText?.text.toString()

            val valido = validaCampos(email, senha, confirmaSenha)

            if (valido) {
                cadastra(Usuario(email, senha))
            }
        }
    }

    private fun cadastra(usuario: Usuario) {
        viewModel.cadastra(usuario).observe(viewLifecycleOwner, Observer {
            it?.let { recurso ->
                if (recurso.dado) {
                    view?.snackBar("Cadastro realizado com sucesso")
                    controlador.popBackStack()
                } else {
                    val mensagemErro = recurso.erro ?: "Ocorreu uma falha no cadastro"
                    view?.snackBar(mensagemErro)
                }
            }
        })
    }

    private fun validaCampos(
        email: String,
        senha: String,
        confirmaSenha: String
    ): Boolean {
        var valido = true

        if (email.isBlank()) {
            cadastro_usuario_email.error = "E-mail é necessário"
            valido = false
        }

        if (senha.isBlank()) {
            cadastro_usuario_senha.error = "Senha é necessária"
            valido = false
        }

        if (senha != confirmaSenha) {
            cadastro_usuario_confirma_senha.error = "Senhas diferentes"
            valido = false
        }
        return valido
    }

    private fun limpaTodosCampos() {
        cadastro_usuario_email.error = null
        cadastro_usuario_senha.error = null
        cadastro_usuario_confirma_senha.error = null
    }

}