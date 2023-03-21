/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.trackingapp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;


public class macAddress {
    public static Integer parseIP(String ip){

        String ipnew=ip.substring(ip.lastIndexOf("."));
        String ip2 = ip.replace(ipnew, "");
        ipnew=ip2.substring(ip2.lastIndexOf("."));
        ip2 = ip2.replace(ipnew, "");
        ip = ip.replace(ip2, "");
        ip = ip.replace(".", "");
        Integer ip_int=Integer.parseInt(ip);
        return ip_int;

    }

    public static Integer getIPAddress(boolean useIPv4) {
        //<uses-permission android:name="android.permission.INTERNET" />
        //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return parseIP(sAddr);
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return null;
    }
       
    }
    
    

