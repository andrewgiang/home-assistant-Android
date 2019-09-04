package io.homeassistant.android.onboarding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.homeassistant.android.R
import io.homeassistant.android.data.model.HomeAssistantInstance
import kotlinx.android.synthetic.main.fragment_discovered_instances.*

class DiscoveredInstancesFragment : Fragment(R.layout.fragment_discovered_instances) {

    private val args by navArgs<DiscoveredInstancesFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foundInstances.text = resources.getQuantityString(
            R.plurals.found_instances,
            args.instances.size,
            args.instances.size
        )
        recyclerView.adapter = InstanceAdapter(args.instances)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}

class InstanceAdapter(private val instances: Array<HomeAssistantInstance>) :
    RecyclerView.Adapter<InstanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.instance_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return instances.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(instances[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.instanceTextView)
        private val urlTextView: TextView = view.findViewById(R.id.urlTextView)

        fun bindView(item: HomeAssistantInstance) {
            itemView.setOnClickListener {

            }
            nameTextView.text = item.name
            urlTextView.text = item.baseUrl
        }

    }
}
