package com.example.studentapp.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studentapp.databinding.LoginBinding
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.studentapp.R
import com.google.android.material.snackbar.Snackbar

class loginFragment : Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var account: Auth0
    private var appJustLaunched = true
    private var userIsAuthenticated = false

    private var user = User()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
        binding.login.setOnClickListener { login() }
        binding.logout.setOnClickListener { logout() }
    }

    private fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(requireContext(), object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    // The user either pressed the “Cancel” button
                    // on the Universal Login screen or something
                    // unusual happened.
                    // getString(R.string.login_failure_message)
                    showSnackBar("FAIL")
                }

                override fun onSuccess(credentials: Credentials) {
                    // The user successfully logged in.
                    val idToken = credentials.idToken
                    user = User(idToken)
                    userIsAuthenticated = true
                    showSnackBar("Success: ${user.name}")
                    updateUI()
                }
            })
    }
    private fun logout() {
        WebAuthProvider
            .logout(account)
            .start(requireContext(), object : Callback<Void?, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    // For some reason, logout failed.
                    // getString(R.string.general_failure_with_exception_code,
                    //                        exception.getCode())
                    showSnackBar("FAIL WITH EXCEPTION")
                }

                override fun onSuccess(payload: Void?) {
                    // The user successfully logged out.
                    user = User()
                    userIsAuthenticated = false
                    updateUI()
                }

            })
    }
    private fun updateUI() {
        if (appJustLaunched) {
            // getString(R.string.initial_title)
            binding.textviewTitle.text = "Initial title"
            appJustLaunched = false
        } else {
            binding.textviewTitle.text = if (userIsAuthenticated) {
                //getString(R.string.logged_in_title)
                "logged in"
            } else {
                //getString(R.string.logged_out_title)
                "logged out"
            }
        }

        binding.login.isEnabled = !userIsAuthenticated
        binding.logout.isEnabled = userIsAuthenticated

        binding.textviewUserProfile.visibility = if (userIsAuthenticated) View.VISIBLE else View.GONE
        binding.textviewUserProfile.text = "User profile" /*getString(R.string.user_profile,
            user.name,
            user.email)*/

        //binding.imageviewUser.isVisible = userIsAuthenticated
        //binding.imageviewUser.loadImage(user.picture)
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }

}