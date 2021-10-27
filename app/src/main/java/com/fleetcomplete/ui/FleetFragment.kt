package com.fleetcomplete.ui


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleetcomplete.R
import com.fleetcomplete.adapter.FleetAdapter
import com.fleetcomplete.clicklisteners.RecyclerViewClickListener
import com.fleetcomplete.models.Response
import com.fleetcomplete.ui.viewmodels.FleetViewModel
import com.fleetcomplete.utils.AppPreference
import com.fleetcomplete.utils.Resource
import com.fleetcomplete.utils.SnackBars
import kotlinx.android.synthetic.main.api_key_dialog.view.*
import kotlinx.android.synthetic.main.fleet_fragment.*
import kotlinx.android.synthetic.main.fleet_toolbar.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/*

kodein DI is added ,it will inject viewmodel constructor params (app &repository)
directly instantiating viewmodel with instance()
 */
/*
         val api = API()
         val repository = AppRepo(api)
         factory = AppModelFactory((requireActivity().application as App)!!, repository)
         viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
         note: with out Kodein we require these object for accessing viewmodel , but kodein simplifies the
         dependency for each classes.
*/
class FleetFragment : Fragment(), RecyclerViewClickListener, KodeinAware {
    override val kodein by kodein()
    private val viewModel: FleetViewModel by instance()// by Kodein Dependency Injection.
    private lateinit var appPreference: AppPreference //for data storage for api key
    private var mAPI_KEY: String = ""
    private var CheckApiKey: Boolean = false
    private lateinit var snackBar: SnackBars //snack bar alerts
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fleet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appPreference = AppPreference(requireActivity())
        snackBar = SnackBars()
        this.lifecycleScope.launch {
            val value = appPreference.read("API_KEY")
            if (value != null) {
                mAPI_KEY = value.toString()
                viewModel.getData(mAPI_KEY)
            } else {

                _showKeyDialoge(mAPI_KEY)

                viewModel.mAPIKEY.observe(viewLifecycleOwner, Observer { apiKey ->
                    if (!apiKey.isEmpty()) {
                        viewModel.getData(apiKey)
                    }
                })
                if (!CheckApiKey) {
                    viewModel.getData(mAPI_KEY)
                }

            }


        }

        this.viewModel.mAPIKEY.observe(viewLifecycleOwner, Observer { apiKey ->
            if (!apiKey.isEmpty()) {
                mAPI_KEY = apiKey
                viewModel.getData(apiKey)
            }
        })

        this.viewModel._lastData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    response.data?.let { GiffResponse ->
                        rv_fleet.also {
                            it.adapter = null
                            it.layoutManager = LinearLayoutManager(activity)
                            it.setHasFixedSize(true)

                            it.adapter = FleetAdapter(response.data.response, this)
                        }
                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        hideProgressBar()
                        rv_fleet.adapter = null
                        if (!mAPI_KEY.isEmpty())
                            snackBar.showSnackBar(
                                "Ooops  error occured: $message",
                                requireActivity()
                            )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        img_btn_reload.setOnClickListener(View.OnClickListener {
            if (mAPI_KEY.isEmpty()) {
                snackBar.showSnackBar("Ooops..Please Enter API Key first.....", requireActivity())
            } else {
                viewModel.getData(mAPI_KEY)

            }
        })

        img_btn_key.setOnClickListener(View.OnClickListener {
            _showKeyDialoge(mAPI_KEY)
        })

    }

    private fun hideProgressBar() {
        pgsbar_fleet.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        pgsbar_fleet.visibility = View.VISIBLE

    }

    private fun _showKeyDialoge(API_KEY: String) {
        try {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = this.getLayoutInflater()

            @SuppressLint("InflateParams")
            val dialogView = inflater.inflate(R.layout.api_key_dialog, null)
            dialogBuilder.setView(dialogView)
            val btn_yes = dialogView.btn_yes
            val btn_no = dialogView.btn_no
            val edt_apikey = dialogView.edt_api_key
            val alertDialog = dialogBuilder.create()
            alertDialog.setTitle("Enter API Key")
            edt_apikey.setText(API_KEY)
            alertDialog.show()
            btn_yes.setOnClickListener {
                val key = edt_apikey.text.toString().trim()
                if (!key.isEmpty()) {
                    this.lifecycleScope.launch {
                        appPreference.save(
                            "API_KEY",
                            key
                        )
                    }
                    alertDialog.dismiss()
                    CheckApiKey = true
                    viewModel.getAPIKEY(key)

                } else {
                    snackBar.showSnackBar("Please Enter API KEY", requireActivity())
                }
            }
            btn_no.setOnClickListener {

                snackBar.showSnackBar("Clicked on Cancel", requireActivity())// showSnackBar()
                alertDialog.dismiss()
            }
        } catch (Ex: Exception) {
            Ex.printStackTrace()
        }
    }

    override fun onRecyclerViewItemClick(fleet: Response, position: Int) {
        val actions =
            FleetFragmentDirections.actionFleetFragmentToMapsFragment(fleet) // navigation to maps fragment
        viewModel.mAPIKEY.postValue("")
        findNavController().navigate(actions)
    }
}