package com.base.net.dns;

import com.base.api.Logger;
import com.base.net.ApiBase;
import com.base.util.collection.ListUtil;
import com.base.watcher.Watcher;
import com.base.watcher.WatcherEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.Dns;

/**
 * Created by haojiangfeng on 2023/12/25.
 */
public class ApiDns implements Dns{

    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String hostname) {
        List<InetAddress> addresses = null;
        Logger.i("lookup->" + hostname);
        try {
            addresses = Dns.SYSTEM.lookup(hostname);
            Logger.textSingle("[DNS] lookup(" + hostname + ") success.");
        } catch (UnknownHostException e) {
            Logger.textSingle("[DNS] lookup(" + hostname + ") parse error, catch UnknownHostException.");
            Logger.d(e);
        } catch (Throwable e){
            Logger.textSingle("[DNS] lookup(" + hostname + ") parse error, exception: " + e.getMessage());
            Logger.d(e);
        }

        if(addresses == null || addresses.size() == 0){
            if(DnsUtil.isAppDomain(hostname)) {
                addresses = changeDomainHost(hostname, addresses);
            } else if(DeeplinkHost.isAppDeeplink(hostname)){
                addresses = changeDeeplinkHost(hostname);
            } else {
                Logger.textSingle("[DNS] lookup(" + hostname + ") error, with no switch address");
            }
        }

        if(addresses == null){
            Logger.textSingle("[DNS] lookup(" + hostname + ") error, addresses is null.");
            return new ArrayList<>();
        } else {
            return addresses;
        }
    }

    private List<InetAddress> changeDomainHost(String hostname, List<InetAddress> addresses){
        if (ListUtil.isNotEmpty(ApiBase.backupDomain)) {
            for (int i = 0; i < ApiBase.backupDomain.size(); i++) {
                String nextDomain = DnsUtil.getNextDomain(hostname);
                String host = DnsUtil.getHostByUrl(nextDomain);
                if (!DnsUtil.isValidIP(host)) {
                    try {
                        addresses = Dns.SYSTEM.lookup(host);
                        Logger.textSingle("ApiDns", "changeDomainHost", "[DNS] lookup(" + hostname + ") error, parse " + nextDomain + " success.");
                    } catch (UnknownHostException e) {
                        Logger.textSingle("ApiDns", "changeDomainHost", "[DNS] lookup(" + hostname + ") error, parse " + nextDomain + " fail, catch UnknownHostException.");
                        Logger.d(e);
                    } catch (Throwable e) {
                        Logger.textSingle("ApiDns", "changeDomainHost", "[DNS] lookup(" + hostname + ") error, parse " + nextDomain + " fail, exception: " + e.getMessage());
                        Logger.d(e);
                    }
                    if (ListUtil.isNotEmpty(addresses)) {
                        Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_SWITCH_DNS, nextDomain);
                        break;
                    }
                } else {
                    addresses = DnsUtil.buildDirectIPAddress(host);
                    Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_SWITCH_DNS, nextDomain);
                    break;
                }
            }
        } else {
            Logger.textSingle("[DNS] lookup(" + hostname + ") backupDomain is empty, with no switch address");
        }
        return addresses;
    }

    private List<InetAddress> changeDeeplinkHost(String hostname){
        String deeplinkBaseurl = DeeplinkHost.getNextHost(hostname);
        Watcher.getInstance().notifyWatcher(WatcherEvent.EVENT_DNS_DEEPLINK, deeplinkBaseurl);
        String ipAddress = DnsUtil.getHostByUrl(deeplinkBaseurl);
        List<InetAddress> addresses = DnsUtil.buildInetIPAddress(hostname, ipAddress);
        return addresses;
    }

}
