package com.example.hdmicectestapp

import android.content.Context
import com.example.hdmicectestapp.HdmiHelper.callbackProxyListener
import android.util.Log
import java.lang.Exception
import java.lang.reflect.InvocationHandler
import kotlin.Throws
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.lang.reflect.Method
import java.lang.reflect.Proxy


class HdmiHelper(context: Context) {

    fun init(context: Context) {
        try {

            //Interface Callback Proxy
            val hotplugEventListenerClass =
                Class.forName("android.hardware.hdmi.HdmiControlManager\$HotplugEventListener")
            val vendorCommandListenerClass =
                Class.forName("android.hardware.hdmi.HdmiControlManager\$VendorCommandListener")
            val oneTouchPlayCallbackClass =
                Class.forName("android.hardware.hdmi.HdmiPlaybackClient\$OneTouchPlayCallback")
            val displayStatusCallbackClass =
                Class.forName("android.hardware.hdmi.HdmiPlaybackClient\$DisplayStatusCallback")
            val interfaceOneTouchPlaybackCallback = Proxy.newProxyInstance(
                oneTouchPlayCallbackClass.classLoader,
                arrayOf(oneTouchPlayCallbackClass),
                callbackProxyListener()
            )
            val interfaceHotplugEventCallback = Proxy.newProxyInstance(
                hotplugEventListenerClass.classLoader,
                arrayOf(hotplugEventListenerClass),
                callbackProxyListener()
            )
            val interfaceDisplayStatusCallbackClass = Proxy.newProxyInstance(
                displayStatusCallbackClass.classLoader,
                arrayOf(displayStatusCallbackClass),
                callbackProxyListener()
            )
            val m = context.javaClass.getMethod("getSystemService", String::class.java)
            val obj_HdmiControlManager = m.invoke(context, "hdmi_control" as Any)
            Log.d(
                "HdmiHelper",
                "obj: " + obj_HdmiControlManager + " | " + obj_HdmiControlManager.javaClass
            )
            for (method in obj_HdmiControlManager.javaClass.methods) {
                Log.d("HdmiHelper", "   method: " + method.name)
            }

            //ENabling volume control
            val method_enableVolumeControl= obj_HdmiControlManager.javaClass.getMethod(
                "setHdmiCecVolumeControlEnabled",  Int::class.java
            )
            method_enableVolumeControl.invoke(obj_HdmiControlManager,1)

            val method_addHotplugEventListener = obj_HdmiControlManager.javaClass.getMethod(
                "addHotplugEventListener",
                hotplugEventListenerClass
            )
            method_addHotplugEventListener.invoke(
                obj_HdmiControlManager,
                interfaceHotplugEventCallback
            )
            val m2 = obj_HdmiControlManager.javaClass.getMethod("getPlaybackClient")
            val obj_HdmiPlaybackClient = m2.invoke(obj_HdmiControlManager)
            Log.d(
                "HdmiHelper",
                "obj_HdmiPlaybackClient: " + obj_HdmiPlaybackClient + " | " + obj_HdmiPlaybackClient.javaClass
            )
            val method_oneTouchPlay = obj_HdmiPlaybackClient.javaClass.getMethod(
                "oneTouchPlay",
                oneTouchPlayCallbackClass
            )
            method_oneTouchPlay.invoke(obj_HdmiPlaybackClient, interfaceOneTouchPlaybackCallback)
            val method_queryDisplayStatus = obj_HdmiPlaybackClient.javaClass.getMethod(
                "queryDisplayStatus",
                displayStatusCallbackClass
            )
            method_queryDisplayStatus.invoke(
                obj_HdmiPlaybackClient,
                interfaceDisplayStatusCallbackClass
            )
            val method_getActiveSource =
                obj_HdmiPlaybackClient.javaClass.getMethod("getActiveSource")
            Log.d(
                "HdmiHelper",
                "getActiveSource: " + method_getActiveSource.invoke(obj_HdmiPlaybackClient)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun PowerOFFTargetTv(context: Context){
        try {
            val hdmiDeviceInfoClass =
                Class.forName("android.hardware.hdmi.HdmiDeviceInfo")

            val m: Method = context.javaClass.getMethod("getSystemService", String::class.java)
            val objHdmicontrolmanager: Any = m.invoke(context, "hdmi_control" as Any)

            val m_DeviceBuilder = hdmiDeviceInfoClass.javaClass.getMethod("cecDeviceBuilder")
            val objectDeviceBuilder = m_DeviceBuilder.invoke(hdmiDeviceInfoClass)


            val m_createhdmiDeviceInfo  = objectDeviceBuilder.javaClass.getMethod("build")
            val objDeviceInfo = m_createhdmiDeviceInfo.invoke(objectDeviceBuilder)
            Log.d(
                "HdmiHelper",
                "obj " + objDeviceInfo +"|"+objDeviceInfo.javaClass
            )
            val m_PowerOffTargetTv: Method =
                objHdmicontrolmanager.javaClass.getMethod("powerOffDevice",hdmiDeviceInfoClass)

            m_PowerOffTargetTv.invoke(objHdmicontrolmanager, objDeviceInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun PowerONTargetTv(context: Context){
        try {
            val hdmiDeviceInfoClass =
                Class.forName("android.hardware.hdmi.HdmiDeviceInfo")

            val m: Method = context.javaClass.getMethod("getSystemService", String::class.java)
            val objHdmicontrolmanager: Any = m.invoke(context, "hdmi_control" as Any)

            val m_DeviceBuilder = hdmiDeviceInfoClass.javaClass.getMethod("cecDeviceBuilder")
            val objectDeviceBuilder = m_DeviceBuilder.invoke(hdmiDeviceInfoClass)


            val m_createhdmiDeviceInfo  = objectDeviceBuilder.javaClass.getMethod("build")
            val objDeviceInfo = m_createhdmiDeviceInfo.invoke(objectDeviceBuilder)
            Log.d(
                "HdmiHelper",
                "obj " + objDeviceInfo +"|"+objDeviceInfo.javaClass
            )
            val m_PowerOffTargetTv: Method =
                objHdmicontrolmanager.javaClass.getMethod("powerOnDevice",hdmiDeviceInfoClass)

            m_PowerOffTargetTv.invoke(objHdmicontrolmanager, objDeviceInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    inner class callbackProxyListener : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            try {
                Log.d(
                    "HdmiHelper",
                    "Start method " + method.name + " | " + proxy.javaClass + " | " + method.declaringClass
                )
                if (args != null) {

                    // Prints the method being invoked
                    for (i in args.indices) {
                        Log.d("HdmiHelper", "  - Arg(" + i + "): " + args[i].toString())
                    }
                }
                if (method.name == "onReceived") {
                    if (args.size == 1) {
                        onReceived(args[0])
                    } else if (args.size == 3) {
                        val bos = ByteArrayOutputStream()
                        val oos = ObjectOutputStream(bos)
                        oos.writeObject(args[1])
                        oos.flush()
                        onReceived(args[0] as Int, bos.toByteArray(), args[2] as Boolean)
                    }
                } else if (method.name == "onComplete") {
                    onComplete(args[0] as Int)
                } else return if (method.name == "toString") {
                    this.toString()
                } else {
                    method.invoke(this, *args)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun onComplete(result: Int) {
            Log.d("HdmiHelper", "onComplete: $result")
        }

        fun onReceived(event: Any) {
            val eventClass: Class<*> = event.javaClass
            Log.d("HdmiHelper", "onReceived(1): $event | $eventClass")
            try {
                val method_getPort = eventClass.getMethod("getPort")
                val method_isConnected = eventClass.getMethod("isConnected")
                val method_describeContents = eventClass.getMethod("describeContents")
                Log.d(
                    "HdmiHelper",
                    "    - " + method_getPort.invoke(event) + " | " + method_isConnected.invoke(
                        event
                    ) + " | " + method_describeContents.invoke(event)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun onReceived(srcAddress: Int, params: ByteArray, hasVendorId: Boolean) {
            Log.d("HdmiHelper", "onReceived(3): $srcAddress | $params | $hasVendorId")
        }
    }

    init {
        init(context)
    }
}