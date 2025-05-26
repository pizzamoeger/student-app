package com.hannah.studentapp.ui.authentification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.hannah.studentapp.MainActivity
import com.hannah.studentapp.R
import com.hannah.studentapp.SharedData
import com.hannah.studentapp.databinding.AuthentificationFragmentBinding
import com.hannah.studentapp.ui.classesItem.ClassesItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthentificationFragment : Fragment() {

    private var _binding: AuthentificationFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val TAG = "AuthFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthentificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            signIn(email, password)
        }

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            signUpIfNew(email, password)
        }

        binding.logoutButton.setOnClickListener {
            signOut()
        }

        if (auth.currentUser == null) {
            binding.logoutButton.visibility = View.GONE
        } else {
            binding.logoutButton.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
            binding.signupButton.visibility = View.GONE
            binding.emailEditText.visibility = View.GONE
            binding.passwordEditText.visibility = View.GONE
        }
    }

    private fun signUpIfNew(email: String, password: String) {
        // Check if user already exists
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        // No existing account -> sign up
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { signUpTask ->
                                if (signUpTask.isSuccessful) {
                                    SharedData.save()
                                    //SharedData.load(requireContext())
                                    val signInIntent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(signInIntent)
                                    Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(requireContext(), "Sign up failed: ${signUpTask.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        // Already exists
                        Toast.makeText(requireContext(), "Email already registered. Please log in.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to check email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    lifecycleScope.launch {
                        ClassesItem.loaded = false
                        SharedData.load(requireContext())
                        while (!ClassesItem.loaded) {
                            delay(50)
                        }
                        val signInIntent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(signInIntent)
                        Toast.makeText(requireContext(), "Logged in!", Toast.LENGTH_SHORT).show()
                        moveToMain()
                    }
                } else {
                    Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun moveToMain() {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigateUp()
    }

    // Function to handle user sign out
    private fun signOut() {
        SharedData.save()
        // Use AuthUI to sign out; it handles cleaning up FirebaseUI's session state
        AuthUI.getInstance()
            .signOut(requireContext()) // Use requireContext() for safety
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User signed out.")
                    // After signing out, navigate back to the sign-in screen
                    // Ensure the containing activity is finished as well if needed
                    val signInIntent = Intent(requireContext(), MainActivity::class.java) // Replace with the Activity that hosts THIS fragment
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(signInIntent)
                    requireContext().getSharedPreferences("shared_data_prefs", Context.MODE_PRIVATE).edit().clear().apply()
                    SharedData.load(requireContext())
                    requireActivity().finish() // Finish the activity hosting this fragment
                } else {
                    Log.w(TAG, "Sign out failed.", task.exception)
                    Toast.makeText(requireContext(), "Sign out failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
