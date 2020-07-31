package br.com.alura.aluraesporte

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.alura.aluraesporte.ui.fragment.BaseFragment
import br.com.alura.aluraesporte.ui.viewmodel.MinhaContaViewModel
import kotlinx.android.synthetic.main.minha_conta.*
import org.koin.android.viewmodel.ext.android.viewModel

class MinhaContaFragment : BaseFragment() {

    private val viewModel: MinhaContaViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.minha_conta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.usuario.observe(viewLifecycleOwner, Observer {
            it?.let {usuario ->
                minha_conta_email.text = usuario.email
            }
        })
    }

}