//package com.benefit.net.util;
//
//import com.base.util.thread.ExecutorsUtils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.charset.StandardCharsets;
//import java.util.Enumeration;
//
//public class IpUtils {
//    /**
//     * 获取内网IP地址的方法
//     *
//     * @return
//     */
//    public static String getIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces(); enNetI
//                    .hasMoreElements(); ) {
//                NetworkInterface netI = enNetI.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = netI.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
//                        String aa = inetAddress.getHostAddress();
//                        System.out.println("当前内网IP测试："+aa);
//                        return inetAddress.getHostAddress();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    /**
//     * 获取外网ip地址的方法1--网页信息格式
//     * @return
//     */
//    public static String getOutNetIP() {
//        String ipAddress = "";
//        try {
//            String address = "http://www.3322.org/dyndns/getip"; //单独只有IP外网地址的API
////            String address = "https://ifconfig.co/json"; //json格式信息的API，使用这个自己搞代码
//            URL url = new URL(address);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setUseCaches(false);
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36"); //设置浏览器ua 保证不出现503
//
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream in = connection.getInputStream();
//                // 将流转化为字符串
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                String tmpString;
//                StringBuilder retJSON = new StringBuilder();
//                while ((tmpString = reader.readLine()) != null) {
//                    retJSON.append(tmpString + "\n");
//                }
//                ipAddress = retJSON.toString();
//            } else {
//                System.out.println("网络连接异常，无法获取IP地址！");
//            }
//        } catch (Exception e) {
//        }
//        return ipAddress;
//    }
//
//    /**
//     * 获取外网ip地址的方法2--Json格式
//     * @return
//     */
//    public static void getNetIp() {
//        ExecutorsUtils.getInstance().getAppExecutors().networkIO().execute(() -> {
//            URL infoUrl;
//            InputStream inStream;
//            String line = "";
//            try {
//                infoUrl = new URL("https://ifconfig.co/json"); //json格式信息的API，使用案例。
//                URLConnection connection = infoUrl.openConnection();
//                HttpURLConnection httpConnection = (HttpURLConnection) connection;
//                int responseCode = httpConnection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    inStream = httpConnection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
//                    StringBuilder strber = new StringBuilder();
//                    while ((line = reader.readLine()) != null) {
//                        strber.append(line + "\n");
//                    }
//                    inStream.close();
//                    // 从反馈的结果中提取出IP地址
//                    int start = strber.indexOf("{");
//                    int end = strber.lastIndexOf("}");
//                    String json = strber.substring(start, end + 1);
//                    if (json != null) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            String netCountry = jsonObject.optString("country");
//                            IpStorage.saveNetCountry(netCountry);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//
//}
