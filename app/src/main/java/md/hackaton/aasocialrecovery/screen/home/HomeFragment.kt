package md.hackaton.aasocialrecovery.screen.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.databinding.FragmentHomeBinding
import md.hackaton.aasocialrecovery.navigateSafe
import md.hackaton.aasocialrecovery.screen.base.BaseFragment
import md.hackaton.aasocialrecovery.screen.recovery.RecoveryDataListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModel<HomeViewModelImpl>()
    override lateinit var viewBinding: FragmentHomeBinding
    private val adapter = RecoveryDataListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.dataRV.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.dataRV.adapter = adapter

        viewBinding.setupRecoveryBtn.setOnClickListener {
            findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToSetupRecoveryFragment())
        }

        viewBinding.recoverBtn.setOnClickListener {
            findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToRecoveryFragment())
        }

        viewBinding.deployBtn.setOnClickListener {
            viewModel.onDeployPress()
        }

        viewBinding.freezeBtn.setOnClickListener {
            viewModel.onFreezePress()
        }

        viewBinding.swipeLayout.setOnRefreshListener {
            viewModel.onRefresh()
        }


        lifecycleScope.launch {
            viewModel.details.collect {data ->
                adapter.data = listOf(
                    RecoveryDataListAdapter.ItemModel("owner", "Owner Address:", data.ownerAddress),
                    RecoveryDataListAdapter.ItemModel("publicKey", "Public key:", data.publicKey),
                    RecoveryDataListAdapter.ItemModel("aa_address", "Abstraction Address:", data.abstractionAddress),
                    RecoveryDataListAdapter.ItemModel("aa_address_deployed", "Abstraction Address Deployed:", data.isAbstractionDeployed.toString()),
                )

                viewBinding.requestAgentBtn.setOnClickListener {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://dev-pqvbe3y0djn7ccoa.us.auth0.com/authorize?response_type=code&client_id=iTjLUV8droepK6WGbsgDY5Ikjd6amPMt&redirect_uri=https://socialrecovery.mind-dev.com/auth0&scope=openid%20profile%20email&state=${data.abstractionAddress}")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorEvent.collect { error ->
                showError(error)
            }
        }

        lifecycleScope.launch {
            viewModel.deployResult.collect { hash ->
                showMessage("Deploy Tx executed. Hash = $hash")
            }
        }

        lifecycleScope.launch {
            viewModel.freezeResult.collect { hash ->
                showMessage("Freeze Tx executed. Hash = $hash")
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                viewBinding.swipeLayout.isRefreshing = isLoading
            }
        }
    }
}