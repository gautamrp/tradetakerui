package com.tradetaker.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tradetaker.dao.NSEBhavCopyRepository
import com.tradetaker.dao.TriggersRepository
import com.tradetaker.domain.Nifty50
import com.tradetaker.domain.TelegramResponse
import com.tradetaker.domain.TriggerResponse
import com.tradetaker.domain.TriggersRequest
import com.tradetaker.entity.NSEBhavCopyEntity
import com.tradetaker.entity.TriggerEntity
import com.tradetaker.utils.TradeTakerConstants
import com.tradetaker.utils.Utility
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import javax.ws.rs.core.UriBuilder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.text.SimpleDateFormat
import java.time.Duration

@Service
@Slf4j
class TradeTakerService {

    @Autowired
    TriggersRepository triggersRepository

    @Autowired
    NSEBhavCopyRepository nseBhavCopyRepository

    @Qualifier('restTemplate')
    @Autowired
    RestTemplate restTemplate

    String processTrigger(TriggersRequest triggersRequest) {
        TriggerResponse triggerResponse
        TriggerEntity triggerEntity

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(triggersRequest))
        String[] stocks = triggersRequest.stocks.split(",")
        String[] trigger_prices = triggersRequest.trigger_prices.split(",")

        for (int i = 0; i < stocks.length; i++) {
            triggerEntity = new TriggerEntity()
            triggerEntity.stock = stocks[i]
            triggerEntity.triggerPrice = Float.parseFloat(trigger_prices[i])
            triggerEntity.triggeredAt = triggersRequest.triggered_at
            triggerEntity.scanName = triggersRequest.scan_name
            triggerEntity.scanURL = triggersRequest.scan_url
            triggerEntity.alertName = triggersRequest.alert_name
            triggerEntity.triggeredDate = Utility.getCurrentDate()
            triggersRepository.save(triggerEntity)
            System.out.println("Triggers save to DB")

            // Send trigger to Telegram
            triggerResponse = prepareForExternalPost(triggerEntity, false)
            postToTelegram(triggerResponse)

            // Send trigger to NHT
            postToNHT(triggerResponse)

            System.out.println("Triggers sent to Telegram")
        }
        return "Received triggers for " + triggersRequest.stocks
    }

    List<TriggerResponse> getTriggerByDate(String triggerDate) {
        List<TriggerResponse> triggerResponses = new ArrayList<>()

        System.out.println("Fetching data for : " + triggerDate)

        List<TriggerEntity> triggersEntity = triggersRepository.findByTriggeredDateOrderByTriggeredAt(triggerDate)

        for (TriggerEntity triggerEntity : triggersEntity) {
            triggerResponses.add(prepareForExternalPost(triggerEntity, false))
        }

        return triggerResponses
    }

    List<String> getTriggerDates() {
        System.out.println("Fetching all trigger dates")
        return triggersRepository.findDistinctByTriggeredDate()
    }

    List<Nifty50> getAllNifty50() {
        List<Nifty50> nifty50List
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nifty50.json")
            Nifty50[] nifty50Arr = new ObjectMapper().readValue(inputStream, Nifty50[].class)
            nifty50List = new ArrayList(Arrays.asList(nifty50Arr))
        }
        catch (Exception e) {
            throw new RuntimeException(e)
        }

        return nifty50List
    }

    /*
    Nifty50 getNifty50(String symbol) {
        List<Nifty50> nifty50List
        Nifty50[] nifty50Arr
        int index = 0
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nifty50.json")
            nifty50Arr = new ObjectMapper().readValue(inputStream, Nifty50[].class)
            //nifty50List = new ArrayList(Arrays.asList(nifty50Arr))

            for (; index < nifty50Arr.length; index++) {
                if (symbol == nifty50Arr[index].symbol)
                    break
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e)
        }

        return nifty50Arr[index]
    }*/

    TriggerResponse prepareForExternalPost(TriggerEntity triggerEntity, boolean isArchive) {
        TriggerResponse triggerResponse = new TriggerResponse()
        //Nifty50 nifty50
        triggerResponse.stock = triggerEntity.stock
        triggerResponse.triggerPrice = triggerEntity.triggerPrice
        triggerResponse.triggeredDate = triggerEntity.triggeredDate
        triggerResponse.triggeredAt = triggerEntity.triggeredAt
        triggerResponse.scanName = triggerEntity.scanName
        triggerResponse.scanURL = triggerEntity.scanURL
        triggerResponse.alertName = triggerEntity.alertName

        if (!isArchive) {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd")
            Date currentExpiry = simpleDateFormat1.parse(nseBhavCopyRepository.findDistinctByExpiryDate().get(0))
            List<NSEBhavCopyEntity> nseBhavCopyList = nseBhavCopyRepository.findBySymbolAndExpiryDate(triggerEntity.stock, currentExpiry)
            // CE
            triggerResponse.resistance = (nseBhavCopyList.get(0).strikePrice > nseBhavCopyList.get(2).strikePrice) ? nseBhavCopyList.get(2).strikePrice + " ~ " + nseBhavCopyList.get(0).strikePrice : nseBhavCopyList.get(0).strikePrice + " ~ " + nseBhavCopyList.get(2).strikePrice
            // PE
            triggerResponse.support = (nseBhavCopyList.get(3).strikePrice > nseBhavCopyList.get(5).strikePrice) ? nseBhavCopyList.get(5).strikePrice + " ~ " + nseBhavCopyList.get(3).strikePrice : nseBhavCopyList.get(3).strikePrice + " ~ " + nseBhavCopyList.get(5).strikePrice

            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyMMM")
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(nseBhavCopyRepository.findDistinctByExpiryDate().get(0))

            triggerResponse.calls = new ArrayList<>()
            triggerResponse.settlePrice = new ArrayList<>()
            int indexA, indexB

            if (nseBhavCopyList.get(0).strikePrice.toInteger() > nseBhavCopyList.get(2).strikePrice.toInteger()) {
                indexA = 2
                indexB = 0
            } else {
                indexA = 0
                indexB = 2
            }

            triggerResponse.calls.add(triggerEntity.stock.trim() + simpleDateFormat2.format(date).toUpperCase() + nseBhavCopyList.get(indexA).strikePrice.toInteger() + nseBhavCopyList.get(indexA).optionType.trim())
            triggerResponse.calls.add(triggerEntity.stock.trim() + simpleDateFormat2.format(date).toUpperCase() + nseBhavCopyList.get(indexB).strikePrice.toInteger() + nseBhavCopyList.get(indexB).optionType.trim())
            triggerResponse.settlePrice.add(nseBhavCopyList.get(indexA).settlePrice.toFloat())
            triggerResponse.settlePrice.add(nseBhavCopyList.get(indexB).settlePrice.toFloat())

            if (nseBhavCopyList.get(3).strikePrice.toInteger() > nseBhavCopyList.get(5).strikePrice.toInteger()) {
                indexA = 5
                indexB = 3
            } else {
                indexA = 3
                indexB = 5
            }

            triggerResponse.calls.add(triggerEntity.stock.trim() + simpleDateFormat2.format(date).toUpperCase() + nseBhavCopyList.get(indexA).strikePrice.toInteger() + nseBhavCopyList.get(indexA).optionType.trim())
            triggerResponse.calls.add(triggerEntity.stock.trim() + simpleDateFormat2.format(date).toUpperCase() + nseBhavCopyList.get(indexB).strikePrice.toInteger() + nseBhavCopyList.get(indexB).optionType.trim())
            triggerResponse.settlePrice.add(nseBhavCopyList.get(indexA).settlePrice.toFloat())
            triggerResponse.settlePrice.add(nseBhavCopyList.get(indexB).settlePrice.toFloat())
        }
        //triggerResponse.info = getNifty50(triggerEntity.stock.trim())
        return triggerResponse
    }

    String postToTelegram(TriggerResponse triggerResponse) throws IOException, InterruptedException {
        System.out.println("Triggers sending to Telegram")
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(triggerResponse))
        String post = Utility.formatTelegramPost(triggerResponse)
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build()
        UriBuilder builder = UriBuilder
                .fromUri(TradeTakerConstants.TG_URI)
                .path("/" + TradeTakerConstants.TG_SB_TOKEN + "/sendMessage")
                .queryParam("chat_id", TradeTakerConstants.TG_SB_CHATID)
                .queryParam("parse_mode", "HTML")
                .queryParam("text", post == "" ? "Oops!" : post)
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build("bot" + TradeTakerConstants.TG_SB_BOTID))
                .timeout(Duration.ofSeconds(5))
                .build()
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString())
        TelegramResponse telegramResponse = new Gson().fromJson(response.body(), TelegramResponse.class)
        System.out.println("telegramResponse : " + response.body())

        return telegramResponse.ok
    }

    void postToNHT(TriggerResponse triggerResponse) {
        TriggersRequest triggersRequest = new TriggersRequest()

        triggersRequest.triggered_at = triggerResponse.triggeredAt
        triggersRequest.webhook_url = TradeTakerConstants.NHT_URL_PROD

        String optionAction_0, optionAction_1
        int indexA, indexB

        // Consider only CE for now
        if ("longFUT" == triggerResponse.scanName) {
            // If LONG - BUY highest strike price and SELL lowest strike price
            optionAction_0 = "BUYFNO-CS"
            optionAction_1 = "SELLFNO-CS"
        } else {
            // If SHORT - SELL highest strike price and BUY highest strike price
            optionAction_0 = "SELLFNO-DS"
            optionAction_1 = "BUYFNO-DS"
        }

        triggersRequest.scan_name = optionAction_0
        triggersRequest.scan_url = optionAction_0
        triggersRequest.alert_name = optionAction_0
        triggersRequest.trigger_prices = triggerResponse.settlePrice.get(0)
        triggersRequest.stocks = triggerResponse.calls.get(0)
        System.out.println(new Gson().toJson(triggersRequest))
        restTemplate.postForEntity(
                TradeTakerConstants.NHT_URL_DEV,
                triggersRequest,
                TriggersRequest.class)
        restTemplate.postForEntity(
                TradeTakerConstants.NHT_URL_PROD,
                triggersRequest,
                TriggersRequest.class)

        triggersRequest.scan_name = optionAction_1
        triggersRequest.scan_url = optionAction_1
        triggersRequest.alert_name = optionAction_1
        triggersRequest.trigger_prices = triggerResponse.settlePrice.get(1)
        triggersRequest.stocks = triggerResponse.calls.get(1)
        System.out.println(new Gson().toJson(triggersRequest))
        restTemplate.postForEntity(
                TradeTakerConstants.NHT_URL_DEV,
                triggersRequest,
                TriggersRequest.class)
        restTemplate.postForEntity(
                TradeTakerConstants.NHT_URL_PROD,
                triggersRequest,
                TriggersRequest.class)
    }

    List<TriggerResponse> getArchiveTriggersForStock(String stock) {
        List<TriggerResponse> triggerResponses = new ArrayList<>()

        System.out.println("Fetching archive data for : " + stock)
        List<TriggerEntity> triggersEntity = triggersRepository.findArchiveTriggersByStock(stock)

        for (TriggerEntity triggerEntity : triggersEntity) {
            triggerResponses.add(prepareForExternalPost(triggerEntity, true))
        }

        return triggerResponses
    }
}
