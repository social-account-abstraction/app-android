package md.hackaton.aasocialrecovery.screen.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.Exception

abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding>: Fragment() {

    abstract val viewModel: VM
    abstract var viewBinding: VB

    fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun showError(exception: Exception) {
        Toast.makeText(requireContext(), "ERROR: " + exception.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}