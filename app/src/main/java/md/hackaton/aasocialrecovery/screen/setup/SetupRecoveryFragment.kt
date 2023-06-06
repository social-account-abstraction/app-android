package md.hackaton.aasocialrecovery.screen.setup

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
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.databinding.FragmentSetupRecoveryBinding
import md.hackaton.aasocialrecovery.navigateSafe
import md.hackaton.aasocialrecovery.screen.base.BaseFragment
import md.hackaton.aasocialrecovery.screen.recovery.RecoveryDataListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetupRecoveryFragment: BaseFragment<SetupRecoveryViewModel, FragmentSetupRecoveryBinding>() {

    override val viewModel: SetupRecoveryViewModel by viewModel<SetupRecoveryViewModelImpl>()
    override lateinit var viewBinding: FragmentSetupRecoveryBinding
    private val adapter = RecoveryDataListAdapter()

    companion object {
        const val SCAN_ADDRESS_RC = "SCAN_ADDRESS_RC"
        const val SCAN_HASH_RC = "SCAN_HASH_RC"
        const val FRAGMENT_RESULT_FIELD = "data"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSetupRecoveryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.addFreezerBtn.isEnabled = false
        viewBinding.addHashBtn.isEnabled = false

        viewBinding.accountsRV.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.accountsRV.adapter = adapter

        setFragmentResultListener(SCAN_ADDRESS_RC) { requestKey, bundle ->
            if (requestKey == SCAN_ADDRESS_RC) {
                val value = bundle.getString(FRAGMENT_RESULT_FIELD) ?: "-"
                viewBinding.addressET.setText(value)
            }
        }

        setFragmentResultListener(SCAN_HASH_RC) { requestKey, bundle ->
            if (requestKey == SCAN_HASH_RC) {
                val value = bundle.getString(FRAGMENT_RESULT_FIELD) ?: "-"
                viewBinding.hashET.setText(value)
            }
        }

        viewBinding.addressET.doOnTextChanged { text, _, _, _ ->
            viewBinding.addFreezerBtn.isEnabled = !text.isNullOrEmpty()
        }


        viewBinding.hashET.doOnTextChanged { text, _, _, _ ->
            viewBinding.addHashBtn.isEnabled = !text.isNullOrEmpty()
        }

        viewBinding.addFreezerBtn.setOnClickListener {
            val address = viewBinding.addressET.text.toString()
            viewModel.onAddFreezer(address)
            viewBinding.addressET.text = null

        }

        viewBinding.addHashBtn.setOnClickListener {
            val signature = viewBinding.hashET.text.toString()
            viewModel.onAddAgent(signature)
            viewBinding.hashET.text = null
        }

        viewBinding.scanAddressBtn.setOnClickListener {
            findNavController().navigateSafe(SetupRecoveryFragmentDirections.actionSetupRecoveryFragmentToScanQRFragment(
                requestCode = SCAN_ADDRESS_RC,
                resultField = FRAGMENT_RESULT_FIELD
            ))
        }


        viewBinding.scanHashBtn.setOnClickListener {
            findNavController().navigateSafe(SetupRecoveryFragmentDirections.actionSetupRecoveryFragmentToScanQRFragment(
                requestCode = SCAN_HASH_RC,
                resultField = FRAGMENT_RESULT_FIELD
            ))
        }

        lifecycleScope.launch {
            viewModel.data.collect { data -> adapter.data = data}
        }

        lifecycleScope.launch {
            viewModel.error.collect { exception ->
                if (exception != null) {
                    Toast.makeText(requireContext(), "ERROR: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.success.collect { result ->
                Toast.makeText(requireContext(), "Success: $result", Toast.LENGTH_SHORT).show()

            }
        }

        lifecycleScope.launch {
            viewModel.data.collect { data -> adapter.data = data}
        }
        viewBinding.submitBtn.setOnClickListener {
            viewModel.onSubmitPress()
        }
    }
}