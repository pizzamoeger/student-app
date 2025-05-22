package com.example.studentapp.ui.authentification

import android.app.Activity.RESULT_OK // Import RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.example.studentapp.databinding.AuthentificationFragmentBinding
import com.example.studentapp.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

// Define a TAG for logging, good practice!
private const val TAG = "AuthentificationFrag"

class AuthentificationFragment : Fragment() {

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth

    private var _binding: AuthentificationFragmentBinding? = null
    private val binding get() = _binding!!

    // Using FirebaseUI (requires adding the firebase-ui-auth dependency)
    // Register the ActivityResultLauncher in the class body, before onCreate
    // We use requireActivity() as the LifecycleOwner because the launcher needs to be
    // tied to the activity's lifecycle when used within a Fragment.
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        // Correctly register the launcher inside onCreate
        signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            onSignInResult(result.idpResponse)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthentificationFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null).
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // No user is signed in, launch the Sign-In flow using FirebaseUI
            Log.d(TAG, "No user signed in. Launching sign-in flow.")
            launchSignInFlow() // Call the function to start the sign-in process
        } else {
            // User is signed in. Proceed to your main app content.
            Log.d(TAG, "User is signed in: ${currentUser.uid}. Proceeding to main content.")
            goToMainActivity() // Call the function to navigate to the main activity
        }
    }

    // Function to handle the result from the FirebaseUI sign-in flow
    // Use IdpResponse for result type, and it can be null if the user cancels
    private fun onSignInResult(result: IdpResponse?) {
        // Check for success by looking at the error property of IdpResponse
        if (result?.error == null) {
            // Successfully signed in
            Log.d(TAG, "Sign in successful!")
            goToMainActivity() // Navigate on success
        } else {
            // Sign in failed.
            val response = result // The result *is* the IdpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled by user")
            } else {
                // Log the specific error for debugging
                Log.w(TAG, "Sign in error", response.error)
                // You might want to show a more specific message based on the error type
            }
            Toast.makeText(
                requireContext(),
                "Sign in failed. Please try again.",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    // Call this when you need to start the sign-in flow (typically in onStart if user is null)
    private fun launchSignInFlow() { // Made private as it's called internally
        // Choose authentication providers you have enabled in the Firebase Console
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        // Add other providers enabled in Firebase Console:
            // AuthUI.IdpConfig.PhoneBuilder().build(),
            // AuthUI.IdpConfig.FacebookBuilder().build(), // Requires additional setup
            // AuthUI.IdpConfig.TwitterBuilder().build() // Requires additional setup
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            // .setLogo(R.mipmap.ic_launcher) // Optional: Add your app logo here (uncomment and replace R.mipmap.ic_launcher with your actual logo resource)
            // .setTheme(R.style.your_auth_theme) // Optional: Apply a custom theme
            .setAvailableProviders(providers)
            .build()

        // Launch the FirebaseUI sign-in activity using the launcher we registered
        signInLauncher.launch(signInIntent)
    }

    // Function to navigate to the main content Activity
    private fun goToMainActivity() {
        // Create an Intent to start your MainActivity
        val intent = Intent(activity, MainActivity::class.java) // Use 'activity' or 'requireActivity()' for context in a Fragment
        // Add flags to clear the back stack if you don't want the user to go back to auth screen
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        // If this fragment is in an Activity that should be finished after successful auth,
        // you might want to finish the containing Activity here as well.
        // requireActivity().finish() // Use requireActivity() for safety
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


    // Don't forget to clean up the binding in onDestroyView to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
