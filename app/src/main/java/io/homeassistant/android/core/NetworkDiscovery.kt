package io.homeassistant.android.core

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import io.homeassistant.android.data.model.HomeAssistantInstance
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val tag = "NetworkDiscovery"

class NetworkDiscovery @Inject constructor(
    val nsdManager: NsdManager
) {
    private val scope = MainScope()

    suspend fun getServices(delayMillis: Long): List<HomeAssistantInstance> =
        suspendCancellableCoroutine { continuation ->
            scope.launch {
                val instances = mutableListOf<HomeAssistantInstance>()
                val resolveCallback = resolveListener(instances)
                val discoveryCallback = discoveryListener(resolveCallback, continuation)
                nsdManager.discoverServices(
                    "_home-assistant._tcp",
                    NsdManager.PROTOCOL_DNS_SD,
                    discoveryCallback
                )
                delay(delayMillis)
                continuation.resume(instances)
                nsdManager.stopServiceDiscovery(discoveryCallback)
            }
        }

    private fun discoveryListener(
        resolveCallback: NsdManager.ResolveListener,
        continuation: CancellableContinuation<List<HomeAssistantInstance>>
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
                continuation.resumeWithException(DiscoverFailed())
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
        return attrMap
    }

}

