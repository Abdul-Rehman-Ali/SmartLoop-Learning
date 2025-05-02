package com.smartloopLearn.learning.student.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartloopLearn.learning.chat.AdminChat.AdminChatActivity
import com.smartloopLearn.learning.chat.AdminChat.UserListActivity
import com.smartloopLearn.learning.chat.UserChatActivity
import com.smartloopLearn.learning.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Phone Call

        binding.phoneCall1.setOnClickListener {
            val number = "+923464298524"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
        }

        binding.phoneCall2.setOnClickListener {
            val number = "+923464298524"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
        }

        binding.phoneCall3.setOnClickListener {
            val number = "+923464298524"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
        }



        // Whatsapp Chat
//        binding.whatsapp1.setOnClickListener {
//            val number = "+923464298524"
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number")
//            startActivity(intent)
//        }

        binding.whatsapp2.setOnClickListener {
            val number = "+923464298524"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number")
            startActivity(intent)
        }

        binding.whatsapp3.setOnClickListener {
            val number = "+923464298524"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number")
            startActivity(intent)
        }

        binding.tvRealchat.setOnClickListener {
            val intent = Intent(requireContext(), UserChatActivity::class.java)
            startActivity(intent)
        }

        binding.tvRealAdminchat.setOnClickListener {
            val intent = Intent(requireContext(), UserListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
