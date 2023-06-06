package md.hackaton.aasocialrecovery.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.databinding.FragmentAuthBinding
import md.hackaton.aasocialrecovery.navigateSafe
import md.hackaton.aasocialrecovery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment: BaseFragment<AuthViewModel, FragmentAuthBinding>() {

    override val viewModel: AuthViewModel by viewModel<AuthViewModelImpl>()
    override lateinit var viewBinding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAuthBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewBinding.sunbitBtn.isEnabled = false
        viewBinding.generateBtn.setOnClickListener { viewModel.onGeneratePress() }
        viewBinding.sunbitBtn.setOnClickListener {
            val pk = viewBinding.pkET.text.toString()
            val address = viewBinding.addressET.text.toString()
            println("AuthFragment.onSubmitPress | address = $address | privateKey = $pk")
            viewModel.onSubmitPress(pk, address)
        }

        viewBinding.pkET.doOnTextChanged { text, _, _, _ -> viewBinding.sunbitBtn.isEnabled = !text.isNullOrEmpty() }

        lifecycleScope.launch {
            viewModel.generatedPkEvent.collect { pk -> viewBinding.pkET.setText(pk)}
        }

        lifecycleScope.launch {
            viewModel.error.collect { e -> viewBinding.pkET.error = e?.message}
        }

        lifecycleScope.launch {
            viewModel.completedEvent.collect { cred ->
                findNavController().navigateSafe(AuthFragmentDirections.actionAuthFragmentToHomeFragment())
            }
        }
    }
}