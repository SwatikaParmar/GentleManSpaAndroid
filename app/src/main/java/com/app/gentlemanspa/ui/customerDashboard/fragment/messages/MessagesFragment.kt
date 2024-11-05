package com.app.gentlemanspa.ui.customerDashboard.fragment.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentMessagesBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.HomeCustomerFragment
import com.app.gentlemanspa.ui.customerDashboard.fragment.messages.adapter.MessagesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.messages.adapter.MessagesAdapter.MessagesCallbacks


class MessagesFragment : Fragment(), View.OnClickListener {
  lateinit var binding:FragmentMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized){
            binding=FragmentMessagesBinding.inflate(layoutInflater,container,false)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick=this
        setMessageAdapter()
    }

    private fun setMessageAdapter() {
        val messagesAdapter=MessagesAdapter()
        binding.rvMessages.adapter=messagesAdapter
        messagesAdapter.setMessagesCallbacks(object : MessagesCallbacks{
            override fun onMessageItemClick() {
              val action=MessagesFragmentDirections.actionMessagesFragmentToChatFragment()
              findNavController().navigate(action)
            }

        })
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
        }
    }
}