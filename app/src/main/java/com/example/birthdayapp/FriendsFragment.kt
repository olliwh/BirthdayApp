package com.example.birthdayapp

import android.graphics.Path.Direction
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.birthdayapp.databinding.FragmentFriendsBinding
import com.example.birthdayapp.models.Friend
import com.example.birthdayapp.models.FriendsViewModel
import com.example.birthdayapp.models.MyAdapter
import com.google.firebase.auth.FirebaseAuth


class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val friendsViewModel : FriendsViewModel by activityViewModels()

    private val binding get() = _binding!!

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userId: String? = auth.currentUser?.email


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner) {friends ->
            binding.recyclerView.visibility = if (friends == null) View.GONE else View.VISIBLE

            binding.textView.text = auth.currentUser?.email
            if (friends != null) {


                val adapter = MyAdapter(friends) { position ->
                    val action =
                    FriendsFragmentDirections.actionFriendsFragmentToSingleFriendFragment(position)
                    findNavController().navigate(action)
                    //findNavController().navigate(R.id.action_friendsFragment_to_singleFriendFragment)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)

                binding.recyclerView.adapter = adapter


                val spinner: Spinner = binding.spinnerSorting
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Log.d("APPLE", "spinner change")

                        if(position == 1){friendsViewModel.sortByName()}
                        if(position == 2){friendsViewModel.sortByNameDescending()}
                        if(position == 3){friendsViewModel.sortByAge()}
                        if(position == 4){friendsViewModel.sortByAgeDescending()}
                        if(position == 5){friendsViewModel.sortByBirth()}
                        if(position == 6){friendsViewModel.sortByBirthDescending()}

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.d("APPLE", "onNothingSelected was called?")

                    }
                }

                binding.buttonFilter.setOnClickListener {
                    val name = binding.edittextFilterName.text.toString().trim()
                    friendsViewModel.filter(name)
                }

            }
            binding.buttonAdd.setOnClickListener {
                findNavController().navigate(R.id.action_friendsFragment_to_addFriendFragment)
            }
            binding.buttonLogout.setOnClickListener {
                auth.signOut()
                findNavController().popBackStack()
            }
        }
        friendsViewModel.reload(userId)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}