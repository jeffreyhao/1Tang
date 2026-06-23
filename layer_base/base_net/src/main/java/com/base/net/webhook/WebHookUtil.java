package com.base.net.webhook;

import android.util.Log;

import com.base.net.ApiBase;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by haojiangfeng on 2025/5/28.
 */
public class WebHookUtil {

    private static final String TAG = "WebHookUtil";

    public static final String VERSION_CRASH_DING     = "version_name_crash_dingtalk";


    public static final int TYPE_WARNING            = 0;
    public static final int TYPE_DEEPLINK           = 1;
    public static final int TYPE_READ               = 2;
    public static final int TYPE_ERROR              = 3;
    public static final int TYPE_REACH_MAX_W        = 4;
    public static final int TYPE_TEST               = 5;
    public static final int TYPE_PURCHASE           = 6;
    public static final int TYPE_ORDER              = 7;




    public static void sendText(String webhookUrl, String json){
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // 发送请求
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "DingTalk response code: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error sending to DingTalk", e);
        }
    }



    public static String getCrashHookToken(Throwable ex){
        String flavor = ApiBase.FLAVOR == null ? "" : ApiBase.FLAVOR.toLowerCase();
        if(ApiBase.DEBUG) {
            switch (flavor) {
                case "reachmax":    return "b0b653624592b259cb02bbb435be46df9362d069fc62b8f085d65ea60f8104f5";
                case "itools":      return "cdc9d274bcbb4781295943dda141c8f29768eac916375a7e0d58e93e17f34430";
                default:            return "5d6c94e3be0baad8ae2f52e0c56196d79b4b7cab1a807cb3f6d59e8e358887de";
            }
        } else {
            if(ex instanceof UnsatisfiedLinkError) {  // SO崩溃
                return "bb152a65a8c7d01932141dfa2a33ecbd30f82ed191c4a31aec2af29f87da5dd7";
            }
            switch (flavor){
                case "novelverse":      return "eb920586b7dec80d47ec705ddd4d20d1f34c72385f3288c3957efbb9dfda7b3a";
                case "noveldaily":      return "209c7b9f7a761e6ce12b02514117402a1f54ebe080e2936f04146abd13860d97";
                case "novelworm":       return "76c06ca23f93f79a379e5dc0ae63f52aaccd6f8f2908c30b624748e8ea766f50";
                case "peaknovel":       return "9696cd305088b3c8317b19ae87133eef3385449ea9201a57481ad5b027c687ed";
                case "novelpal":        return "87fa1712177142073ff6033b63e99c8f4f7a9faff7e92f482ed2ba105dd021f3";
                case "novelhome":       return "7ed6b549d46ae8432919b95da714ddb3a26903083a1c29aba38efc57edb843e0";
                case "novelclub":       return "7c8eb238d7e88e1e5152571072abee68baf6fa0cdcb8c1e1278b330fa67d01fb";
                case "novelplant":      return "0eba32f044de8f3ee016dbdae6c21f787ed2c15889d73902ac312cecc9c08382";
                case "lycannovel":      return "859bcbdf82df9ec59a806e6674dad9cc19bd59e05652e29faafbbee7d0ec2dcb";
                case "novelvault":      return "426e88868085fe048143bcf7839046cb7aa0d494bbc090392f14e5f33801b590";

                case "lycanfiction":    return "942092f913bc78d4585170f069e40a3561104beef5847fbe7c186fe7789c63e8";
                case "alphafiction":    return "fd7c18afeae5a27694efc893a2fb4f33628dd0ad3a8c0b84a77578f29c01a5af";
                case "wolffiction":     return "85ff1c6c79408e53ffda8408ff5d8edc688f700028daeba5b908e099cd455bdb";

                case "romanfic":        return "b3fc0a334b14d4622c3d559f80fc813a2a81a009bde515c4172832e5d427c5e4";
                case "novelnook":       return "16fb16d57317a999d01296d3c7bd12e05e1c1629aaaae852cdb622f1ea469979";

                case "novelhive":       return "911b9db38fe8f6556f0dcb55fedd975897919464d14c09f4197f9712dbdab116";
                case "novelmuse":       return "cb004a83924868a3de274680fbf9e59d592d3e28096ca1b3c9dcd4d91053a955";

                case "reachmax":        return "b0b653624592b259cb02bbb435be46df9362d069fc62b8f085d65ea60f8104f5";
            }
        }
        return "";
    }



    public static String getFeedbackHookToken(){
        String flavor = ApiBase.FLAVOR == null ? "" : ApiBase.FLAVOR.toLowerCase();
        switch (flavor){
            case "novelclub":       return "ecd795d419c505b14ff87f07f251e2a63d99d6831eb048f775276269c773cec2";
            case "lycannovel":      return "125a609c24a451e9ab220a074a3a6c228454db579db8a8fc2acf0470d6ec45c7";
            case "romanfic":        return "48e5ad55edaeeaed36fbf6c79faff1e7f0fe6109f6f38115fd824181cd220be0";
            case "novelnook":       return "f57628ceb8c07901c51ed2e8c7ef1cf94cda1130a7b3f98527d2cb6f95ed7404";
            case "storynest":       return "2b3502181ffa74da467b323520bb00341a4508293c96d1fe3d3bafcc32bd6a46";
            case "lycanfiction":    return "a2994ab5b15f148f43052e2d738ccdc7ee4f4668d48c48d40af0ef9741b8eb57";
            case "novelverse":      return "d3ca7c21ae9520b9957d3cb43aaeb00a6e716e50b1f4399ca041535ae14bd50e";
            case "novelvault":      return "f0191b285d15259a81f96a879a14efbc84ec197bfeb5c5396d23e7d20c4deb09";

            case "novelhive":       return "373aa374d000a69de7302739ad81875ecb9bb2d919d41ba3fe283987f0415d6e";
            case "novelhome":       return "ed2a28db7e7ab01f37f5ce98f4b9f204f69bc7ed4f9eac0554a715e60c75407b";
            case "novelpal":        return "108ed799d8b3ad926d1bc8078d94f9b7f79c29c4ffb6d3095ad815324492cb31";
            case "alphafiction":    return "0e631765aa7b1e770d882e54c0d5deb0c9e9a9df2b4b0e3159505fd19265a8b2";
            case "novelworm":       return "7e0190030c7f46d23e4caf95751015d29afc1614c64a5035acd8bce15532b4c9";
            case "wolffiction":     return "14fbd3b8a4cdf31ea2e1cc6e6570443166d964d29c900e6626c263e880c819af";
            case "novelplant":      return "633c8dbfc331d91f1aea26b2c2625ed22487ce16ca652193953b375b7cbcaccc";
            case "peaknovel":       return "85d3fe994e2720a3e25c678bcb3dba1024e0a1d0166da335cba93438b8d76bd9";
            case "novelmuse":       return "48c3b3a75e6f101187b0b5346583bdc7ac2e2af1f9395f4fb3dbeedf8b6065a4";
            default:                return "";
        }
    }


    public static String getMonitorHookToken(int type){
        switch (type) {
            case TYPE_DEEPLINK:      return "019510850444ca12b6f6a221ff434ef4bb2d428d9d4b5353740165a922882a97";
            case TYPE_READ:          return "6c87727909f9b475d518a9bdbb278311894d3c557bd95ad9cd703cf0b2440051";
            case TYPE_ERROR:         return "1436bdef2834342eab18cd6d89fb6c2728655c9e30502275c4ef3d69590dbb6d";
            case TYPE_REACH_MAX_W:   return "b22fa83f3f0d05d4b748390e31b625e4684a172d1dc32577a2d8c999c3d2ae0e";
            case TYPE_WARNING:       return "ac1c4a11f93939ef3310f697dde7a85c959dbe2ff15c857dbff8aad3bddb8dc4";
            case TYPE_TEST:          return "899902596b1147832d213d1489772c5bdec4b1acab180bcc92e42e44b3066818";
            case TYPE_PURCHASE:      return "722f38bcbe4c612a16cae75e4e8775f04869b8ca210f4543b478dac4c5e7a5ed";
            case TYPE_ORDER:         return "9354eb6d582d895362f522dc2231b120c2d6093ff157a9a7d70c47e50c9077be";
            default:                                return "";
        }
    }



}
