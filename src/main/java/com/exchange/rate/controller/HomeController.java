package com.exchange.rate.controller;

import com.exchange.rate.model.CurrencylayerEnum;
import com.exchange.rate.model.QuotesVO;
import com.exchange.rate.runner.AppStartupRunner;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

import static com.exchange.rate.model.CurrencylayerEnum.*;
import static com.exchange.rate.runner.AppStartupRunner.QUOTES;
import static org.springframework.util.StringUtils.*;

@RestController
public class HomeController {

    private static final Gson GSON = new Gson();

    @GetMapping(value = "/exchange-rate/{remittanceCountry}/{country}")
    public ResponseEntity<String> exchangeRate(
            @PathVariable(value = "country", required=false) String country
            , @PathVariable(value = "remittanceCountry", required=false) String remittanceCountry) {

        Map<String, Object> map = new HashMap<>();

        if(!hasText(remittanceCountry) || "undefined".equals(remittanceCountry)) {
            map.put("msg", "송금국가 정보가 존재하지 않습니다.");
            map.put("money", 0.0d);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GSON.toJson(map));
        }

        if(!hasText(country) || "undefined".equals(country)) {
            map.put("msg", "수취국가 정보가 존재하지 않습니다.");
            map.put("money", 0.0d);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GSON.toJson(map));
        }

        QuotesVO quotesVO = QUOTES.get(remittanceCountry);
        if(ObjectUtils.isEmpty(quotesVO)) {
            map.put("msg", "환율정보가 존재하지 않습니다.");
            map.put("money", 0.0d);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GSON.toJson(map));
        }

        Double money = quotesVO.getQuotes(remittanceCountry+country);
        money = money != null ? money : 0.0d;

        map.put("msg", "조회되었습니다.");
        map.put("money", money);

        return ResponseEntity.status(HttpStatus.OK).body(GSON.toJson(map));
    }

}
