package com.fleetcomplete.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ParseException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fleetcomplete.R
import com.fleetcomplete.models.Response
import com.fleetcomplete.models.RowData
import com.fleetcomplete.ui.viewmodels.MapsViewModel
import com.fleetcomplete.utils.AppPreference
import com.fleetcomplete.utils.SnackBars
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.maps_toolbar.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapsFragment : Fragment(), KodeinAware {
    override val kodein by kodein() //dependency injection
    private lateinit var Results: Response
    private lateinit var MapResults: RowData
    private val viewModel: MapsViewModel by instance() // by kodein (no need to initialize viewmodelfact,api,app etc.)
    private lateinit var FromDate: String
    private var mAPIKEY: String = ""
    private lateinit var appPreference: AppPreference  //data storage
    private lateinit var snackBar: SnackBars //snack bar alerts
    private val callback = OnMapReadyCallback { googleMap ->


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.

         */
        try {
            val res = MapResults

            if (res.response.size > 0) {

                googleMap.clear()
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                var points: ArrayList<LatLng>?
                var markerStart: MarkerOptions? = MarkerOptions()
                var markerEnd: MarkerOptions? = MarkerOptions()
                points = ArrayList()
                var _mapSize = MapResults.response.size

                //adding location points from response
                for (i in 0.._mapSize - 1) {
                    points.add(
                        LatLng(
                            MapResults.response[i].Latitude,
                            MapResults.response[i].Longitude
                        )
                    )
                }
                if (points[0].latitude > 0) {
                    markerStart!!.position(points[0]).title("Start")
                } else {
                    markerStart!!.position(points[1]).title("Start")
                }
                markerEnd!!.position(points[_mapSize - 1]).title("End").visible(true)
                val circleDrawable = resources.getDrawable(R.drawable.location)
                val markerIcon: BitmapDescriptor = getMarkerIconFromDrawable(circleDrawable)
                markerEnd.icon(markerIcon)
                markerStart.icon(markerIcon)
                val polylineOptions = PolylineOptions()
                polylineOptions.color(Color.BLUE)
                polylineOptions.width(5f)
                polylineOptions.addAll(points)
                polylineOptions.geodesic(true)
                googleMap.addPolyline(polylineOptions)
                googleMap.addMarker(markerStart)
                googleMap.addMarker(markerEnd)

                val googlePlex = CameraPosition.builder()
                    .target(LatLng(points[_mapSize - 1].latitude, points[_mapSize - 1].longitude))
                    .zoom(10f)
                    .bearing(0f)
                    .tilt(45f)
                    .build()
                googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(googlePlex),
                    5000,
                    null
                )
                pgs_map.visibility = View.INVISIBLE
            } else {
                pgs_map.visibility = View.INVISIBLE
                googleMap.clear()
            }

        } catch (Ex: Exception) {
            Ex.printStackTrace()
        }


    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            appPreference = AppPreference(requireActivity())
            snackBar = SnackBars()
            arguments.let {
                val args = it?.let { Bundle ->
                    MapsFragmentArgs.fromBundle((Bundle))
                }
                if (args != null) {
                    Results = args.response
                }
            }
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            FromDate = sdf.format(Date())
            System.out.println(" C DATE is  " + FromDate)
            editTextDate.setText(FromDate)

            val date: Date
            try {
                date = sdf.parse(FromDate)
                var dateFormat = SimpleDateFormat("yyyy-MM-dd")
                FromDate = dateFormat.format(date)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            txt_palte.setText("Location History: " + Results.plate)
            _getTripDetails(FromDate)

            this.viewModel.rowData.observe(viewLifecycleOwner, Observer { result ->
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                if (result.response.size > 0) {
                    MapResults = result

                    mapFragment?.getMapAsync(callback)
                } else {
                    pgs_map.visibility = View.INVISIBLE
                    MapResults = result
                    mapFragment?.getMapAsync(callback)
                    snackBar.showSnackBar("Ooops No Records Found...!!", requireActivity())
                }
            })
            this.viewModel._distance.observe(viewLifecycleOwner, Observer { distance ->

                txt_distance.setText("Trip Distance : " + distance + " km")
            })
            editTextDate.setOnClickListener(View.OnClickListener {
                var mYear = 0
                var mMonth = 0
                var mDay = 0
                val c = Calendar.getInstance()
                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                getActivity()?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            val mnth = monthOfYear + 1
                            val month = mnth.toString()
                            val day = dayOfMonth.toString()
                            editTextDate.setText((if (day.length == 1) "0$day" else day) + "-" + (if (month.length == 1) "0$month" else month) + "-" + year)
                            val FromDat: String = editTextDate.getText().toString()
                            val dateString = FromDat
                            var dateFormat = SimpleDateFormat("dd-MM-yyyy")

                            val date: Date
                            try {
                                date = dateFormat.parse(dateString)
                                dateFormat = SimpleDateFormat("yyyy-MM-dd")
                                FromDate = dateFormat.format(date)
                                _getTripDetails(FromDate)
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }
                        }, mYear, mMonth, mDay
                    )
                }?.show()
            })
            imgbtn_back.setOnClickListener(View.OnClickListener {
                findNavController().popBackStack()
            })
//            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//            mapFragment?.getMapAsync(callback)
        } catch (Ex: Exception) {
            Ex.printStackTrace()
        }
    }

    /*
    NOTE:
    for getting trip details of specific date(2021-10-25)

    begin time is 00:00:00.00 (2021-10-2500:00:00.00 )
    end time  is 11:59:59 (2021-10-2511:59:59)
    passed to api

     */
    private fun _getTripDetails(Frmdate: String) {
        try {
            pgs_map.visibility = View.VISIBLE
            val kk = mAPIKEY
            this.lifecycleScope.launch {
                val value = appPreference.read("API_KEY")
                mAPIKEY = value.toString()
                viewModel.getData(
                    Frmdate + "T00:00:00.000Z",
                    Frmdate + "T11:59:59.000Z",
                    Results.objectId,
                    mAPIKEY

                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}