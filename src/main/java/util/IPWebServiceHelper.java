package util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alex on 17.19.3.
 */
public class IPWebServiceHelper {
    public static String getCountry(String ip) throws IOException {
        String sURL = "http://freegeoip.net/json/" + ip;

        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        String countryCode = root.getAsJsonObject().get("country_code").getAsString();

        return countryCode == null || countryCode.isEmpty() ? "LV" : countryCode;
    }
}
