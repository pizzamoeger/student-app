package com.example.studentapp.ui.authentification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studentapp.MainActivity
import com.example.studentapp.databinding.AuthentificationFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

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
                                    Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT).show()
                                    goToMainActivity()
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
                    Toast.makeText(requireContext(), "Logged in!", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                } else {
                    Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Function to handle user sign out
    private fun signOut() {
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
                    requireActivity().finish() // Finish the activity hosting this fragment
                } else {
                    Log.w(TAG, "Sign out failed.", task.exception)
                    Toast.makeText(requireContext(), "Sign out failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
