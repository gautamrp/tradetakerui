package com.tradetaker.controller


import com.tradetaker.domain.TriggerResponse
import com.tradetaker.domain.TriggersRequest
import com.tradetaker.service.TradeTakerService
import com.tradetaker.utils.TradeTakerConstants
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(TradeTakerConstants.APP_CONTEXT_ROOT)
@Slf4j
// Required for local testing
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "https://tradetaker.s3.ap-south-1.amazonaws.com/ui/*")
class TradeTakerController {

    @Autowired
    TradeTakerService tradeTakerService

    // http://localhost:8080/tradetaker/v1/health
    // Displays the health of API
    @GetMapping("/health")
    ResponseEntity health() {
        ResponseEntity.status(HttpStatus.OK).body("API is up and running.")
    }

    // http://localhost:8080/tradetaker/v1/nifty50
    // Give the list of Nifty 50 stocks
    @GetMapping("/nifty50")
    ResponseEntity allNifty50() {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.getAllNifty50())
    }

    // http://localhost:8080/tradetaker/v1/triggers
    // Exposed to webhook, where the external triggers are posted here.
    @RequestMapping(value = '/trigger', method = RequestMethod.POST)
    ResponseEntity processTrigger(
            @RequestBody TriggersRequest triggersRequest
    ) {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.processTrigger(triggersRequest))
    }

    // http://localhost:8080/tradetaker/v1/triggerdates
    // Gets the list of dates when trigger was received.
    @RequestMapping(value = '/triggerdates', method = RequestMethod.GET)
    ResponseEntity getTriggerDates() {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.getTriggerDates())
    }

    // http://localhost:8080/tradetaker/v1/triggers/03-03-2023
    // Gets the list of triggers for that particular date.
    @RequestMapping(value = '/trigger/{triggerdate}', method = RequestMethod.GET)
    ResponseEntity getTriggerByDate(@PathVariable(value = 'triggerdate', required = true) String triggerDate) {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.getTriggerByDate(triggerDate))
    }

    // http://localhost:8080/tradetaker/v1/telegram
    // Post the call to Telegram
    @RequestMapping(value = '/telegram', method = RequestMethod.POST)
    ResponseEntity<String> postToTelegram(@RequestBody TriggerResponse triggerResponse) {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.postToTelegram(triggerResponse))
    }

    // http://localhost:8080/tradetaker/v1/nht
    // Post the call to NHT
    @RequestMapping(value = '/nht', method = RequestMethod.POST)
    ResponseEntity<String> postToNHT(@RequestBody TriggerResponse triggerResponse) {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.postToNHT(triggerResponse))
    }

    // http://localhost:8080/tradetaker/v1/archive/INFY
    // Gets alltriggers for that particular stock.
    @RequestMapping(value = '/archives/{stock}', method = RequestMethod.GET)
    ResponseEntity getArchiveTriggersForStock(@PathVariable(value = 'stock', required = true) String stock) {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(tradeTakerService.getArchiveTriggersForStock(stock))
    }
}
