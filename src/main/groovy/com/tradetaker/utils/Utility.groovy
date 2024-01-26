package com.tradetaker.utils


import com.tradetaker.domain.TriggerResponse
import org.springframework.web.util.UriUtils

import java.security.SecureRandom
import java.text.SimpleDateFormat

class Utility {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    static String getCurrentDate() {
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy")
        Date date = new Date()
        sd.setTimeZone(TimeZone.getTimeZone("IST"))
        return sd.format(date).toString()
    }

    static String getTriggerDirPath() {
        return System.getProperty("user.dir") + "/triggers/"
    }

    static String getTriggerFilePath() {
        return getTriggerDirPath() + Utility.getCurrentDate() + ".json"
    }

    static String getTriggerFilePath(String date) {
        return System.getProperty("user.dir") + "/triggers/" + date + ".json"
    }

    static String getNSEURI(String symbol) {
        return (symbol == "NIFTY" || symbol == "BANKNIFTY") ? TradeTakerConstants.OPTION_INDEX_JSON_URI + symbol : TradeTakerConstants.OPTION_EQ_JSON_URI + symbol
    }

    static String formatTelegramPost(TriggerResponse triggerResponse) {
        String url = TradeTakerConstants.OPTION_URI + triggerResponse.stock.trim()
        String post = "Scanner: " + triggerResponse.scanName.trim() + " || Triggered @" + " " + triggerResponse.triggeredAt.trim() + "IST || Stock :<a href=\"" + url + "\"><b>" + triggerResponse.stock.trim() + "</b></a> || Price: " + triggerResponse.triggerPrice + " || Support: " + triggerResponse.support.trim() + " || Resistance: " + triggerResponse.resistance.trim()
        return UriUtils.encodePath(post, "UTF-8")
    }
}
