package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty


class Nifty50 {
    @JsonProperty(value = 'companyName')
    String companyName
    @JsonProperty(value = 'industry')
    String industry
    @JsonProperty(value = 'symbol')
    String symbol
    @JsonProperty(value = 'series')
    String series
    @JsonProperty(value = 'isinCode')
    String isinCode
}
