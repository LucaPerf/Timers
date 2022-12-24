package it.pdm.timers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import it.pdm.timers.PasswordDimenticataActivity
import it.pdm.timers.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val tv_forgot_password = view.findViewById<TextView>(R.id.tv_modifica_password)

        tv_forgot_password.setOnClickListener {
            val intent = Intent(this.requireContext(), PasswordDimenticataActivity::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return view
    }
}