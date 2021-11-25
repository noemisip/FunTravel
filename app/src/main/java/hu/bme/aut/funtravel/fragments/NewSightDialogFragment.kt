package hu.bme.aut.funtravel.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import hu.bme.aut.funtravel.data.Sight
import hu.bme.aut.funtravel.databinding.FragmentNewsightBinding

class NewSightDialogFragment : DialogFragment() {

    interface NewSightDialogListener {
        fun onSightCreated(newItem: Sight)
    }

    private lateinit var listener: NewSightDialogListener

    private lateinit var binding: FragmentNewsightBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewSightDialogListener
            ?: throw RuntimeException("no")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =  FragmentNewsightBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("New sight")
            .setView(binding.root)
            .setPositiveButton("Ok") { dialogInterface, i ->
                if (isValid()) {
                    listener.onSightCreated(getSight())
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        const val TAG = "NewSightDialogFragment"
    }

    private fun isValid() : Boolean {
        return binding.etName.text.isNotEmpty() &&
                binding.etlink.text.isNotEmpty() &&
                binding.etloc.text.isNotEmpty() &&
                binding.etprice.text.isNotEmpty()


    }

    private fun getSight() = Sight(
        0,binding.etName.text.toString(),binding.etloc.text.toString(),binding.etlink.text.toString(),binding.etprice.text.toString().toDouble(),"", false)

}