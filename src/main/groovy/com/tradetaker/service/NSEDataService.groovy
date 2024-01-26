package com.tradetaker.service

import com.tradetaker.dao.NSEBhavCopyRepository
import com.tradetaker.domain.LevelsResponse
import com.tradetaker.entity.NSEBhavCopyEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.text.SimpleDateFormat

@Component
class NSEDataService {
    @Autowired
    NSEBhavCopyRepository nseBhavCopyRepository

    List<LevelsResponse> findBySymbolAndExpiryDate(String symbol, String expiryDate) {
        List<LevelsResponse> levelsResponses = new ArrayList<>()
        LevelsResponse levelsResponse
        List<NSEBhavCopyEntity> nseBhavCopyList

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

        if (null == symbol && null == expiryDate) {
            // Find all levels
            nseBhavCopyList = nseBhavCopyRepository.findAll()
        } else if (null == symbol && null != expiryDate) {
            // Find levels based on expiry date
            nseBhavCopyList = nseBhavCopyRepository.findByExpiryDate(simpleDateFormat.parse(expiryDate))
        } else {
            // Find levels based on symbol and expiry date
            nseBhavCopyList = nseBhavCopyRepository.findBySymbolAndExpiryDate(symbol, simpleDateFormat.parse(expiryDate))
        }

        for (int index = 0; index < nseBhavCopyList.size(); index += 6) {
            levelsResponse = new LevelsResponse()
            levelsResponse.symbol = nseBhavCopyList.get(index).symbol.trim()
            levelsResponse.resistance1 = nseBhavCopyList.get(index).strikePrice.toString()
            levelsResponse.resistance2 = nseBhavCopyList.get(index + 1).strikePrice.toString()
            levelsResponse.resistance3 = nseBhavCopyList.get(index + 2).strikePrice.toString()
            levelsResponse.support1 = nseBhavCopyList.get(index + 3).strikePrice.toString()
            levelsResponse.support2 = nseBhavCopyList.get(index + 4).strikePrice.toString()
            levelsResponse.support3 = nseBhavCopyList.get(index + 5).strikePrice.toString()
            levelsResponses.add(levelsResponse)
        }

        return levelsResponses
    }

    List<String> getExpiryDates() {
        return nseBhavCopyRepository.findDistinctByExpiryDate()
    }

    List<String> getExtractDate() {
        return nseBhavCopyRepository.findDistinctByExtractDate()
    }
}