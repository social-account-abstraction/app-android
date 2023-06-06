package md.hackaton.aasocialrecovery.screen.scan

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.databinding.FragmentScanQrBinding
import md.hackaton.aasocialrecovery.screen.base.BaseFragment
import md.hackaton.aasocialrecovery.screen.recovery.RecoveryFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanQRFragment: BaseFragment<ScanQRViewModel, FragmentScanQrBinding>() {
    private val args by navArgs<ScanQRFragmentArgs>()
    override val viewModel: ScanQRViewModel by viewModel<ScanQRViewModelImpl>()
    override lateinit var viewBinding: FragmentScanQrBinding
    private val codeScanner by lazy { CodeScanner(requireContext(), viewBinding.scannerView) }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            codeScanner.startPreview()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentScanQrBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onPause() {
        super.onPause()
        codeScanner.stopPreview()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeScanner.decodeCallback = DecodeCallback { result ->
            lifecycleScope.launch(Dispatchers.Main) {
                setFragmentResult(args.requestCode, bundleOf(args.resultField to result.text))
                findNavController().navigateUp()
            }

        }
    }
}