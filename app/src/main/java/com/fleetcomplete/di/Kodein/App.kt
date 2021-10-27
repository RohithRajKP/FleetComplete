package com.fleetcomplete.di.Kodein

import android.app.Application
import com.fleetcomplete.network.API
import com.fleetcomplete.repository.FleetRepo
import com.fleetcomplete.repository.MapRepo
import com.fleetcomplete.ui.FleetModelFactory
import com.fleetcomplete.ui.viewmodels.FleetViewModel
import com.fleetcomplete.ui.MapModelFactory
import com.fleetcomplete.ui.viewmodels.MapsViewModel


import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@App)) //aPP instance
        bind() from singleton { API() }//network intance
        bind() from singleton { FleetRepo(instance()) } //fleet repo initialization
        bind() from singleton { MapRepo(instance()) }//map screen repo initailization
        bind() from provider {
            FleetModelFactory(this@App, instance())
        }//fleet viewmodel fact requires Applicatn and FleetRepo which can be supplied from instance
        bind() from provider { MapModelFactory(instance()
        ) } //map viewmodedl factory ,requres api instnce
        bind() from provider {
            FleetViewModel(
                instance(),
                instance()
            )
        } // fleet viewmodel requires app and fleet repo from instnce
        bind() from provider { MapsViewModel(
            instance()
        )
        } //map viewmodel initial


    }


}