package dev.robert.notekeeper.ui.fragments.add_notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import dev.robert.notekeeper.databinding.FragmentAddNotesBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddNotesFragment : Fragment() {

    private var _binding: FragmentAddNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}