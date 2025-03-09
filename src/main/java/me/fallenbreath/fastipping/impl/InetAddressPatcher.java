package me.fallenbreath.fastipping.impl;

import com.google.common.net.InetAddresses;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressPatcher
{
    @SuppressWarnings("UnstableApiUsage")
    public static InetAddress patch(String hostName, InetAddress addr) throws UnknownHostException
    {
        if (InetAddresses.isInetAddress(hostName))
        {
            addr = InetAddress.getByAddress(addr.getHostAddress(), addr.getAddress());
        }
        return addr;
    }
}
