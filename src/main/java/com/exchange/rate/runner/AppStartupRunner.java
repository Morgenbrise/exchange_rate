package com.exchange.rate.runner;

import com.exchange.rate.model.CurrencylayerEnum;
import com.exchange.rate.model.QuotesVO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@Configuration
public class AppStartupRunner implements ApplicationRunner {

    public static String currencylayerKey;
    private static final Gson GSON = new Gson();
    public static Map<String, QuotesVO> QUOTES = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StringBuffer sb = null;
        BufferedReader reader = null;
        URL url = new URL(currencylayerKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if(!isEmpty(conn)) {
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            try {
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    sb = new StringBuffer();
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                    Map<String, Object> map = GSON.fromJson(sb.toString(), Map.class);
                    if(parseBoolean(valueOf(map.get(CurrencylayerEnum.SUCCESS.getValue()))) && map.containsKey(CurrencylayerEnum.QUOTES.getValue()))  {
                        QUOTES.put(
                                valueOf(map.get(CurrencylayerEnum.SOURCE.getValue()))
                                , new QuotesVO((Map<String, Double>)map.get(CurrencylayerEnum.QUOTES.getValue()))
                        );
                    }
                }
            } catch (IOException ioe) {

            } finally {
                reader.close();
            }
        }
    }

    @Value("${property.api.currencylayer.key}")
    private void setClientId(String key) {
        currencylayerKey = key;
    }

}
