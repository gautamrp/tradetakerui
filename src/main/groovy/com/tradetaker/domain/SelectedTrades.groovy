package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty

//@JsonPropertyOrder({"date", "symbol", "ltp", "sl", "trigger", "quantity", "gt", "tradeType", "status", "selected"})
class SelectedTrades {
    @JsonProperty(value = 'date')
    String Date
    @JsonProperty(value = 'symbol')
    String symbol
    @JsonProperty(value = 'ltp')
    String LTP
    @JsonProperty(value = 'sl')
    String SL
    @JsonProperty(value = 'trigger')
    String Trigger
    @JsonProperty(value = 'quantity')
    String Quantity
    @JsonProperty(value = 'gtt')
    String GTT
    @JsonProperty(value = 'tradeType')
    String TradeType
    @JsonProperty(value = 'status')
    String Status
    @JsonProperty(value = 'selected')
    boolean selected
}
