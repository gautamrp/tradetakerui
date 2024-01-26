package com.tradetaker.controller

import com.tradetaker.domain.LevelsResponse
import com.tradetaker.service.NSEDataService
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
class NseDataController {
    @Autowired
    NSEDataService nseDataService

    // Gets the Option levels(Max CE, SMax CE, Max PE, SMax PE)
    @GetMapping("/levels/{expirydate}/{symbol}")
    public ResponseEntity<List<LevelsResponse>> findBySymbolAndExpiryDate(@PathVariable(value = 'symbol', required = true) String symbol,
                                                                          @PathVariable(value = 'expirydate', required = true) String expiryDate
    ) {
        ResponseEntity.status(HttpStatus.OK).body(nseDataService.findBySymbolAndExpiryDate(symbol.trim().toUpperCase(), expiryDate)) as ResponseEntity<List<LevelsResponse>>
    }

    // Gets the Option levels(Max CE, SMax CE, Max PE, SMax PE)
    @GetMapping("/levels/{expirydate}")
    public ResponseEntity<List<LevelsResponse>> findByExpiryDate(@PathVariable(value = 'expirydate', required = true) String expiryDate
    ) {
        ResponseEntity.status(HttpStatus.OK).body(nseDataService.findBySymbolAndExpiryDate(null, expiryDate)) as ResponseEntity<List<LevelsResponse>>
    }

    // Gets the Option levels(Max CE, SMax CE, Max PE, SMax PE)
    @GetMapping("/levels")
    public ResponseEntity<List<LevelsResponse>> findAllLevels() {
        ResponseEntity.status(HttpStatus.OK).body(nseDataService.findBySymbolAndExpiryDate(null, null)) as ResponseEntity<List<LevelsResponse>>
    }

    // http://localhost:8080/tradetaker/v1/expirydates
    // Gets the list of expiry dates from Bhavcopy
    @RequestMapping(value = '/expirydates', method = RequestMethod.GET)
    ResponseEntity<List<String>> getExpiryDates() {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(nseDataService.getExpiryDates()) as ResponseEntity<List<String>>
    }

    // http://localhost:8080/tradetaker/v1/extractdate
    // Gets the extractdate of Bhavcopy
    @RequestMapping(value = '/extractdate', method = RequestMethod.GET)
    ResponseEntity<List<String>> getExtractDate() {
        ResponseEntity.status(HttpStatus.ACCEPTED).body(nseDataService.getExtractDate()) as ResponseEntity<List<String>>
    }
}
