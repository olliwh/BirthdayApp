package com.example.birthdayapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.birthdayapp.models.FriendsViewModel
import com.example.birthdayapp.models.Friend
import com.example.birthdayapp.databinding.FragmentSingleFriendBinding
import java.time.LocalDate
import java.time.Period

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SingleFriendFragment : Fragment() {
    private var _binding: FragmentSingleFriendBinding? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val binding get() = _binding!!

    private val friendsViewModel: FriendsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val singleFriendFragmentArgs: SingleFriendFragmentArgs = SingleFriendFragmentArgs.fromBundle(bundle)
        val position = singleFriendFragmentArgs.position
        val friend = friendsViewModel[position]

        if(friend == null){
            binding.textviewMessage.text = "No such friend!"
            return
        }
        binding.editTextName.setText(friend.name)
        binding.editTextDay.setText(friend.birthDayOfMonth.toString())
        binding.editTextMonth.setText(friend.birthMonth.toString())
        binding.editTextYear.setText(friend.birthYear.toString())
        binding.textViewAge.setText("Age: " + friend.age.toString())

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonDelete.setOnClickListener {
            friendsViewModel.delete(friend.id)
            findNavController().popBackStack()
        }
        fun getSnackBar(view: View) {
            Snackbar.make(view, "Date not valid", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.buttonUpdate.setOnClickListener {
            val user = auth.currentUser
            val name = binding.editTextName.text.toString().trim()
            val day = binding.editTextDay.text.toString().trim().toInt()
            val month = binding.editTextMonth.text.toString().trim().toInt()
            val year = binding.editTextYear.text.toString().trim().toInt()
            if(month > 12) {
                getSnackBar(view)
                return@setOnClickListener
            }else if(day > 31) {
                getSnackBar(view)
                return@setOnClickListener
            }else if(year % 4 == 0 && month == 2 && day > 29){
                getSnackBar(view)
                return@setOnClickListener
            }else if(year % 4 != 0 &&month == 2 && day > 28) {
                getSnackBar(view)
                return@setOnClickListener
            }else if((month == 4 || month == 6 || month == 9 || month ==11)&& day>30) {
                getSnackBar(view)
                return@setOnClickListener
            }
            val age = 1
            val updatedFriend = Friend(friend.id, user?.email.toString(), name, year, month, day, age)
            friendsViewModel.update(updatedFriend)
            findNavController().popBackStack()
        }



    }

}