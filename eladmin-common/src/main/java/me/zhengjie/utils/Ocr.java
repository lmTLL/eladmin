package me.zhengjie.utils;

import com.baidu.aip.ocr.AipOcr;

public class Ocr {
    //设置APPID/AK/SK
    public static final String APP_ID = "16001611";
    public static final String API_KEY = "FWpPCMevFUru9eLlOfHh6Ccj";
    public static final String SECRET_KEY = "Qs8SSNYosUWw7WvZgnNWv5HqmCohVb5Q";

    private static AipOcr client;

    private Ocr() {
    }

    public static void init() {
        client = null;
    }

    public static AipOcr getInstance() {
        if (client == null) {
            synchronized (AipOcr.class) {
                if (client == null) {
                    client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
                    client.setConnectionTimeoutInMillis(2000);
                    client.setSocketTimeoutInMillis(60000);

                }
            }
        }
        return client;
    }
}