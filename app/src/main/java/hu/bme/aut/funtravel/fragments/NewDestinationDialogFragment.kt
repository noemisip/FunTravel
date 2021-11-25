package hu.bme.aut.funtravel.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import hu.bme.aut.funtravel.data.Destination
import hu.bme.aut.funtravel.databinding.FragmentNewdestinationBinding

class NewDestinationDialogFragment : DialogFragment() {

    interface NewDestinationDialogListener {
        fun onDestinationCreated(newItem: Destination)
    }

    private lateinit var listener: NewDestinationDialogListener

    private lateinit var binding: FragmentNewdestinationBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewDestinationDialogListener
            ?: throw RuntimeException("no")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =  FragmentNewdestinationBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("New destination")
            .setView(binding.root)
            .setPositiveButton("Ok") { dialogInterface, i ->
                if (isValid()) {
                    listener.onDestinationCreated(getDestination())
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        const val TAG = "NewDestinationDialogFragment"
    }

    private fun isValid() = binding.etDest.text.isNotEmpty()

    private fun getDestination() = Destination(
        0, binding.etDest.text.toString(),""

    )

}
