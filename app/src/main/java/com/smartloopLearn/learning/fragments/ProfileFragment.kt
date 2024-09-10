package com.smartloopLearn.learning.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.smartloopLearn.learning.*
import com.smartloopLearn.learning.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.admin.DashboardUserActivity
import com.smartloopLearn.learning.databinding.DialogEditProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private val sharedPrefFile = "smartloopLearnPrefs"
    private val profileImageUriKey = "profile_image_uri"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        // Initialize the image picker launcher
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == androidx.fragment.app.FragmentActivity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let { uri ->
                    saveProfileImageUri(uri)
                    binding.profileImage.setImageURI(uri)
                }
            }
        }

        // Load profile image from SharedPreferences
        loadProfileImage()

        if (currentUser != null) {
            // Fetch user data from Firestore
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val phone = document.getString("phone")

                        binding.profileUsername.text = name ?: "No Username"
                        binding.profileEmail.text = email ?: "No Email"
                        binding.profilePhone.text = phone ?: "No Phone Number"
                    } else {
                        Toast.makeText(requireContext(), "No user data found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to fetch user data: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                }

            // Show edit profile dialog when the edit button is clicked
            binding.editProfile.setOnClickListener {
                showEditProfileDialog(currentUser)
            }

            // Open gallery to select a new profile image when profile image is clicked
            binding.profileImage.setOnClickListener {
                openGallery()
            }
        }

        // Set up logout and delete user functionality
        binding.logoutLayout.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }

        binding.deleteUserLayout.setOnClickListener {
            val user = auth.currentUser
            user?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.changePasswordLayout.setOnClickListener {
            val i = Intent(requireContext(), CreateNewPassword::class.java)
            requireContext().startActivity(i)
            requireActivity().finish()
        }

        binding.aboutUsLayout.setOnClickListener {
            val i = Intent(requireContext(), AboutUs::class.java)
            requireContext().startActivity(i)
        }

        binding.privacyPolicyLayout.setOnClickListener {
            val i = Intent(requireContext(), PrivacyPolicy::class.java)
            requireContext().startActivity(i)
        }

        binding.feedbackLayout.setOnClickListener {
            val i = Intent(requireContext(), Feedback::class.java)
            requireContext().startActivity(i)
        }

        binding.newsLayout.setOnClickListener {
            val i = Intent(requireContext(), DashboardUserActivity::class.java)
            requireContext().startActivity(i)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun saveProfileImageUri(uri: Uri) {
        val sharedPreferences = requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(profileImageUriKey, uri.toString())
            apply()
        }
    }

    private fun loadProfileImage() {
        val sharedPreferences = requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val uriString = sharedPreferences.getString(profileImageUriKey, null)
        uriString?.let {
            val uri = Uri.parse(it)
            binding.profileImage.setImageURI(uri)
        }
    }

    private fun showEditProfileDialog(user: FirebaseUser?) {
        if (user == null) return

        val dialogBinding = DialogEditProfileBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogBinding.root)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save") { _, _ ->
                val newName = dialogBinding.editUserName.text.toString().trim()
                val newEmail = dialogBinding.editUserEmail.text.toString().trim()
                val newPhone = dialogBinding.editUserPhone.text.toString().trim()

                val profileUpdates = userProfileChangeRequest {
                    if (newName.isNotEmpty()) displayName = newName
                }

                val emailUpdateTask = if (newEmail.isNotEmpty() && newEmail != user.email) {
                    user.updateEmail(newEmail)
                } else {
                    null
                }

                val firestoreUpdates = mutableMapOf<String, Any>()
                if (newName.isNotEmpty()) firestoreUpdates["name"] = newName
                if (newPhone.isNotEmpty()) firestoreUpdates["phone"] = newPhone
                if (newEmail.isNotEmpty()) firestoreUpdates["email"] = newEmail

                val authUpdateTask = if (newName.isNotEmpty()) {
                    user.updateProfile(profileUpdates)
                } else {
                    null
                }

                val allTasks = mutableListOf<Task<Void>>()
                authUpdateTask?.let { allTasks.add(it) }
                emailUpdateTask?.let { allTasks.add(it) }

                if (firestoreUpdates.isNotEmpty()) {
                    val firestoreTask = firestore.collection("users").document(user.uid)
                        .update(firestoreUpdates)
                    allTasks.add(firestoreTask)
                }

                Tasks.whenAllComplete(allTasks).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (newName.isNotEmpty()) binding.profileUsername.text = newName
                        if (newPhone.isNotEmpty()) binding.profilePhone.text = newPhone
                        if (newEmail.isNotEmpty()) binding.profileEmail.text = newEmail

                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_LONG).show()
                    }
                }
            }

        val dialog = builder.create()
        dialog.show()

        dialogBinding.editUserName.setText(user.displayName)
        dialogBinding.editUserEmail.setText(user.email)
        dialogBinding.editUserPhone.setText(user.phoneNumber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Destroy the binding when the fragment is destroyed
        _binding = null
    }
}
