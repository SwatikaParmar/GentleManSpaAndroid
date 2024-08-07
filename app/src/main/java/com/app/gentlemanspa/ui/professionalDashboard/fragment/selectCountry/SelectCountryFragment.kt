package com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.databinding.FragmentSelectCountryBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.adapter.SelectStateAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.country.CountryItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.states.StateItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.viewModel.SelectCountryViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.adapter.SelectCountryAdapter
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class SelectCountryFragment : Fragment(), View.OnClickListener {

    private var stateList: ArrayList<StateItem> = ArrayList()
    private var countryList: ArrayList<CountryItem> = ArrayList()
    private var countryId: Int = 0
    private lateinit var selectStateAdapter: SelectStateAdapter
    private var type: Int = 0
    private lateinit var binding: FragmentSelectCountryBinding
    private  val viewModel: SelectCountryViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }
    private lateinit var selectCountryAdapter: SelectCountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectCountryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initUI()
    }

    private fun initUI() {
        if (arguments != null) {
            type = requireArguments().getInt("type")
            countryId = requireArguments().getInt("countryId")
        }

        if (type == 1) {
            viewModel.getCountry()
            binding.headerTitle.text ="Select Country"
            binding.etSearch.hint ="Search for Country"
            setSearchCountry()
        } else {
            viewModel.countryId.set(countryId)
            viewModel.getStates()
            binding.etSearch.hint ="Search for State"
            binding.headerTitle.text ="Select State"
            setSearchState()
        }


        onclick()
    }

    private fun onclick() {
        binding.onClick = this
    }

    private fun setSearchCountry() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.search.set(binding.etSearch.text.toString())
                viewModel.getCountry()
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun setSearchState() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.search.set(binding.etSearch.text.toString())
                viewModel.countryId.set(countryId)
                viewModel.getStates()
            }

            override fun afterTextChanged(s: Editable) {
            }

        })
    }


    private fun initObserver() {

        viewModel.resultCountry.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                    }

                    Status.SUCCESS -> {
                         countryList.clear()
                        it.data?.data?.let { it1 -> countryList.addAll(it1) }
                        setCountryAdapter()
                       //  showToast(it.data?.messages.toString())
                    }

                    Status.ERROR -> {
                        if (it.message != null) {
                            requireActivity().showToast(it.message.toString())
                        }
                    }


                }

            }
        }

        viewModel.resultStates.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                    }

                    Status.SUCCESS -> {
                        stateList.clear()
                        it.data?.data?.let { it1 -> stateList.addAll(it1) }
                        setStateAdapter()
                        //  showToast(it.data?.messages.toString())
                    }

                    Status.ERROR -> {
                        if (it.message != null) {
                            requireActivity().showToast(it.message.toString())
                        }
                    }


                }

            }
        }


    }

    private fun setStateAdapter() {
        selectStateAdapter = SelectStateAdapter(stateList, requireContext(), this@SelectCountryFragment)
        binding.rvSelectCountry.adapter = selectStateAdapter
    }

    private fun setCountryAdapter() {
        selectCountryAdapter = SelectCountryAdapter(countryList, requireContext(), this@SelectCountryFragment)
        binding.rvSelectCountry.adapter = selectCountryAdapter
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.ivBack -> {
                findNavController().popBackStack()
            }
        }
    }


}