package io.homeassistant.android.core

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.util.Log
import io.homeassistant.android.BuildConfig
import io.homeassistant.android.data.model.HomeAssistantInstance
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject

const val tag = "NetworkDiscovery"

class NetworkDiscovery @Inject constructor(val nsdManager: NsdManager) {

    suspend fun start(discoverWaitMillis: Long): List<HomeAssistantInstance> {
        val instances = mutableListOf<HomeAssistantInstance>()
        val discoveryCallback = startDiscovery(instances)
        val registerListener = startPublish()

        delay(discoverWaitMillis)

        nsdManager.stopServiceDiscovery(discoveryCallback)
        nsdManager.unregisterService(registerListener)
        return instances
    }

    private fun startDiscovery(instances: MutableList<HomeAssistantInstance>): NsdManager.DiscoveryListener {
        val resolveCallback = resolveListener(instances)
        val discoveryCallback = discoveryListener(resolveCallback)
        nsdManager.discoverServices(
            "_home-assistant._tcp",
            NsdManager.PROTOCOL_DNS_SD,
            discoveryCallback
        )
        return discoveryCallback
    }

    private fun startPublish(): NsdManager.RegistrationListener {
        val nsp = NsdServiceInfo().apply {
            serviceName = Build.MODEL
            serviceType = "_hass-mobile-app._tcp"
            port = 65535
            setAttribute("buildNumber", BuildConfig.VERSION_NAME)
            setAttribute("versionNumber", BuildConfig.VERSION_CODE.toString())
            setAttribute("permanentID", UUID.randomUUID().toString())
        }
        val registerListener = registerListener()
        nsdManager.registerService(
            nsp,
            NsdManager.PROTOCOL_DNS_SD,
            registerListener
        )
        return registerListener
    }

    private fun registerListener(): NsdManager.RegistrationListener {
        return object : NsdManager.RegistrationListener {
            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                Log.d(tag, "onUnregistrationFailed + $serviceInfo : $errorCode")
            }

            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
                Log.d(tag, "onServiceUnregistered + $serviceInfo ")

            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                Log.d(tag, "onRegistrationFailed + $serviceInfo : $errorCode")
            }

            override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
                Log.d(tag, "onServiceRegistered + $serviceInfo")
            }

        }
    }

    private fun discoveryListener(
        resolveCallback: NsdManager.ResolveListener
    ): NsdManager.DiscoveryListener {
        return object : NsdManager.DiscoveryListener {
            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                Log.d(tag, "onServiceFound")
                nsdManager.resolveService(serviceInfo, resolveCallback)
            }

            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.d(tag, "onStopDiscoveryFailed")
            }

            override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.d(tag, "onStartDiscoveryFailed")
                throw DiscoveryFailed()
            }

            override fun onDiscoveryStarted(serviceType: String?) {
                Log.d(tag, "onDiscoveryStarted")
            }

            override fun onDiscoveryStopped(serviceType: String?) {
                Log.d(tag, "onDiscoveryStopped")
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
                Log.d(tag, "onServiceLost")
            }

        }
    }

    private fun resolveListener(instances: MutableList<HomeAssistantInstance>): NsdManager.ResolveListener {
        return object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {}

            override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                instances += HomeAssistantInstance.fromMap(attributeMap(serviceInfo))
            }

        }
    }

    private fun attributeMap(serviceInfo: NsdServiceInfo?): Map<String, String> {
        val attrMap: MutableMap<String, String> = mutableMapOf()
        serviceInfo?.attributes?.forEach { (key, value) ->
            attrMap[key] = String(value, Charsets.UTF_8)
        }
        attrMap["name"] = serviceInfo?.serviceName.orEmpty()
        attrMap["announcedFrom"] = serviceInfo?.host?.hostAddress.orEmpty()
        Log.d(tag, "announcedFrom ${attrMap["announcedFrom"]}")
        return attrMap
    }

}