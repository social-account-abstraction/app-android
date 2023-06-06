package md.hackaton.aasocialrecovery.screen.recovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.databinding.FragmentRecoveryBinding
import md.hackaton.aasocialrecovery.navigateSafe
import md.hackaton.aasocialrecovery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecoveryFragment: BaseFragment<RecoveryViewModel, FragmentRecoveryBinding>() {

    companion object {
        const val SCAN_CURRENT_AGENT_RC = "SCAN_CURRENT_AGENT_RC"
        const val SCAN_NEW_AGENT_RC = "SCAN_NEW_AGENT_RC"
        const val FRAGMENT_RESULT_FIELD = "data"
    }

    override val viewModel: RecoveryViewModel by viewModel<RecoveryViewModelImpl>()
    override lateinit var viewBinding: FragmentRecoveryBinding
    private val adapter: RecoveryDataListAdapter = RecoveryDataListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRecoveryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.addCurrentAgentBtn.isEnabled = false
        viewBinding.addNewAgentBtn.isEnabled = false

        viewBinding.dataRV.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.dataRV.adapter = adapter

        setFragmentResultListener(SCAN_CURRENT_AGENT_RC) { requestKey, bundle ->
            if (requestKey == SCAN_CURRENT_AGENT_RC) {
                val secret = bundle.getString(FRAGMENT_RESULT_FIELD) ?: "-"
                viewBinding.currentAgentHashET.setText(secret)
            }
        }

        setFragmentResultListener(SCAN_NEW_AGENT_RC) { requestKey, bundle ->
            if (requestKey == SCAN_NEW_AGENT_RC) {
                val secret = bundle.getString(FRAGMENT_RESULT_FIELD) ?: "-"
                viewBinding.newAgentHashET.setText(secret)
            }
        }

        viewBinding.scanNewAgentHashBtn.setOnClickListener {
            findNavController().navigateSafe(RecoveryFragmentDirections.actionRecoveryFragmentToScanQRFragment(
                requestCode = SCAN_NEW_AGENT_RC,
                resultField = FRAGMENT_RESULT_FIELD
            ))
        }

        viewBinding.scanCurrentAgentHashBtn.setOnClickListener {
            findNavController().navigateSafe(RecoveryFragmentDirections.actionRecoveryFragmentToScanQRFragment(
                requestCode = SCAN_CURRENT_AGENT_RC,
                resultField = FRAGMENT_RESULT_FIELD
            ))
        }

        viewBinding.addCurrentAgentBtn.setOnClickListener {
            val secret = viewBinding.currentAgentHashET.text.toString()
            viewModel.addCurrentAgentHash(secret)
            viewBinding.currentAgentHashET.text = null
        }

        viewBinding.addNewAgentBtn.setOnClickListener {
            val secret = viewBinding.newAgentHashET.text.toString()
            viewModel.addNewAgentHash(secret)
            viewBinding.newAgentHashET.text = null
        }

        viewBinding.currentAgentHashET.doOnTextChanged { text, _, _, _ ->
            viewBinding.addCurrentAgentBtn.isEnabled = !text.isNullOrEmpty()
        }

        viewBinding.newAgentHashET.doOnTextChanged { text, _, _, _ ->
            viewBinding.addNewAgentBtn.isEnabled = !text.isNullOrEmpty()
        }

        lifecycleScope.launch {
            viewModel.data.collect { data -> adapter.data = data}
        }

        lifecycleScope.launch {
            viewModel.errorEvent.collect { exception ->
                showError(exception)
            }
        }

        lifecycleScope.launch {
            viewModel.successEvent.collect { hash ->
                showMessage("Success: $hash")
            }
        }

        viewBinding.submitBtn.setOnClickListener {
            viewModel.onSubmitPress()
        }

    }
}