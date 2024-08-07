package  com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemSelectCountryBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.SelectCountryFragment
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.country.CountryItem


class SelectCountryAdapter(
    var countryList: ArrayList<CountryItem>,
    var requireContext: Context,
    var  selectCountryFragment: SelectCountryFragment
) : RecyclerView.Adapter<SelectCountryAdapter.ViewHolder>() {

    class ViewHolder(val binding : ItemSelectCountryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSelectCountryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return countryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvTitle.text = countryList[position].countryName

            root.setOnClickListener {
                val intent = Intent("DataCountry")
                intent.putExtra("Country", countryList[position])
                LocalBroadcastManager.getInstance(requireContext).sendBroadcast(intent)
                selectCountryFragment.findNavController().popBackStack(R.id.selectCountryFragment,true)
            }
        }
    }
}