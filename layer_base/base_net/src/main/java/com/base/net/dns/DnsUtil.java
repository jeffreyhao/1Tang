package com.base.net.dns;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.base.api.Logger;
import com.base.api.PreferenceApi;
import com.base.net.ApiBase;
import com.base.util.DevicesUtil;
import com.base.util.collection.ListUtil;
import com.base.util.thread.ExecutorsUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haojiangfeng on 2025/5/6.
 */
public class DnsUtil {


    public static List<InetAddress> buildInetIPAddress(String host, String ip){
        if(TextUtils.isEmpty(ip)) {
            return new ArrayList<>();
        }
        List<InetAddress>  addresses = new ArrayList<>();
        try {
            addresses.add(InetAddress.getByAddress(host, ip.getBytes(StandardCharsets.UTF_8)));
            Logger.textSingle("[DNS] buildDirectAddress: " + ip + " success.");

        } catch (Throwable e){
            Logger.textSingle("[DNS] buildDirectAddress: " + ip + " error: " + e.getMessage());
            Logger.exception(e);
        }
        return addresses;
    }


    /**
     * @return IP地址 76.223.7.214
     */
    public static List<InetAddress> buildDirectIPAddress(String ip){
        if(TextUtils.isEmpty(ip)) {
            return new ArrayList<>();
        }
        List<InetAddress>  addresses = new ArrayList<>();
        try {
            addresses.add(InetAddress.getByAddress(ip, ip.getBytes(StandardCharsets.UTF_8)));
            Logger.textSingle("[DNS] buildDirectAddress: " + ip + " success.");

        } catch (Throwable e){
            Logger.textSingle("[DNS] buildDirectAddress: " + ip + " error: " + e.getMessage());
            Logger.exception(e);
        }
        return addresses;
    }

    public static boolean isValidIP(String ip) {
        if(TextUtils.isEmpty(ip)) {
            return false;
        }
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipPattern);
    }

    public static boolean isAppDomain(String hostName) {
        Logger.textSingle("ApiDns", "isAppDomain", "isAppDomain->hostName: " + hostName);
        if(TextUtils.isEmpty(hostName)) {
            return false;
        }
        if(hostName.contains(ApiBase.BASE_HOST)) {
            return true;
        }
        if(ListUtil.isNotEmpty(ApiBase.backupDomain)) {
            for(String item: ApiBase.backupDomain) {
                String host = getHostByUrl(item);
                if(TextUtils.equals(hostName, host)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static String getHostByUrl(String url) {
        if(!TextUtils.isEmpty(url)) {
            if(!url.startsWith("http")) {
                return url;
            }
        }else{
            return "";
        }
        Uri uri = Uri.parse(url);
        if(uri!=null) {
            String host = uri.getHost();
            return host==null? "" : host;
        }
        return "";
    }

    public static String getNextDomain(String hostName) {
        if(TextUtils.isEmpty(hostName)) {
            return ApiBase.BASE_URL;
        }
        String defaultDomain = PreferenceApi.get("defaultDomain", "");
        String defaultHost = getHostByUrl(defaultDomain);
        if(TextUtils.equals(hostName,defaultHost)) {
            PreferenceApi.put("defaultDomain", ApiBase.BASE_URL);
        }
        Logger.textSingle("ApiDns", "getNextDomain", "hostName: " + hostName);
        if(ListUtil.isNotEmpty(ApiBase.backupDomain)) {
            String firstBackupUrl = ApiBase.backupDomain.get(0);
            if(TextUtils.equals(hostName, ApiBase.BASE_HOST) && !TextUtils.isEmpty(firstBackupUrl)) {
                return firstBackupUrl;
            }
            int currentIndex = -1;
            for(int i=0; i<ApiBase.backupDomain.size(); i++) {
                String hostByUrl = getHostByUrl(ApiBase.backupDomain.get(i));
                if(TextUtils.equals(hostName, hostByUrl)) {
                    currentIndex = i;
                    break;
                }
            }
            if(currentIndex!=-1) {
                if(currentIndex+1<ApiBase.backupDomain.size()) {
                    return ApiBase.backupDomain.get(currentIndex+1);
                }else{
                    return ApiBase.BASE_URL;
                }
            }
        }
        return ApiBase.BASE_URL;
    }



    /**
     * 获取dns
     * @param context
     * @return
     */
    public static void getDns(Context context) {
        ExecutorsUtils.getInstance().getAppExecutors().networkIO().execute(()->{
            try{
                String[] dnsServers = getDnsFromCommand();
                if (dnsServers.length == 0) {
                    dnsServers = getDnsFromConnectionManager(context);
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < dnsServers.length; i++) {
                    if(!TextUtils.isEmpty(dnsServers[i])
                            &&isValidIPAddress(dnsServers[i])
                            &&!isInnerIP(dnsServers[i])) {
                        sb.append(dnsServers[i]);
                        sb.append(",");
                    }
                }
                String result = sb.toString();
                if(result.length()>1 && result.endsWith(",")) {
                    result = result.substring(0,result.length()-1);
                }
                DevicesUtil.saveDNS(result);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null) && (!ipAddress.isEmpty())) {
            return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
        }
        return false;
    }

    /**
     * 私有IP：
     * A类  10.0.0.0-10.255.255.255  
     * B类  172.16.0.0-172.31.255.255  
     * C类  192.168.0.0-192.168.255.255           
     * 127这个网段是环回地址
     * localhost  
     * 判断IP是否内网IP
     * @return boolean
     */
    private static boolean isInnerIP(String ipAddress) {
        List<Pattern> ipFilterRegexList = new ArrayList<>();
        Set<String> ipFilter = new HashSet<String>();
        ipFilter.add("^10\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])"
                + "\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])" + "\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])$");
        // B类地址范围: 172.16.0.0---172.31.255.255
        ipFilter.add("^172\\.(1[6789]|2[0-9]|3[01])\\" + ".(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])\\"
                + ".(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])$");
        // C类地址范围: 192.168.0.0---192.168.255.255
        ipFilter.add("^192\\.168\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])\\"
                + ".(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[0-9])$");
        ipFilter.add("127.0.0.1");
        ipFilter.add("0.0.0.0");
        ipFilter.add("localhost");
        for (String tmp : ipFilter) {
            ipFilterRegexList.add(Pattern.compile(tmp));
        }
        boolean isInnerIp = false;
        for (Pattern tmp : ipFilterRegexList) {
            Matcher matcher = tmp.matcher(ipAddress);
            if (matcher.find()) {
                isInnerIp = true;
                break;
            }
        }
        return isInnerIp;
    }


    //通过 getprop 命令获取
    private static String[] getDnsFromCommand() {
        LinkedList<String> dnsServers = new LinkedList<>();
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            InputStream inputStream = process.getInputStream();
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
            String line;
            while ((line = lnr.readLine()) != null) {
                int split = line.indexOf("]: [");
                if (split == -1) continue;
                String property = line.substring(1, split);
                String value = line.substring(split + 4, line.length() - 1);
                if (property.endsWith(".dns")
                        || property.endsWith(".dns1")
                        || property.endsWith(".dns2")
                        || property.endsWith(".dns3")
                        || property.endsWith(".dns4")) {
                    InetAddress ip = InetAddress.getByName(value);
                    if (ip == null) continue;
                    value = ip.getHostAddress();
                    if (value == null) continue;
                    if (value.length() == 0) continue;
                    dnsServers.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dnsServers.isEmpty() ? new String[0] : dnsServers.toArray(new String[dnsServers.size()]);
    }


    private static String[] getDnsFromConnectionManager(Context context) {
        LinkedList<String> dnsServers = new LinkedList<>();
        if (Build.VERSION.SDK_INT >= 21 && context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    for (Network network : connectivityManager.getAllNetworks()) {
                        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                        if (networkInfo != null && networkInfo.getType() == activeNetworkInfo.getType()) {
                            LinkProperties lp = connectivityManager.getLinkProperties(network);
                            for (InetAddress addr : lp.getDnsServers()) {
                                dnsServers.add(addr.getHostAddress());
                            }
                        }
                    }
                }
            }
        }
        return dnsServers.isEmpty() ? new String[0] : dnsServers.toArray(new String[dnsServers.size()]);
    }

}
