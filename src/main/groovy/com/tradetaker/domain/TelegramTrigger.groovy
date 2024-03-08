package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty

class TelegramTrigger {
    @JsonProperty(value = 'stock')
    public String stock
    @JsonProperty(value = 'trigger_price')
    public String trigger_price
    @JsonProperty(value = 'triggered_time')
    public String triggered_time
    @JsonProperty(value = 'triggered_at')
    public String triggered_date
    @JsonProperty(value = 'scan_name')
    public String scan_name
    @JsonProperty(value = 'status')
    String status
    @JsonProperty(value = 'selected')
    boolean selected
}
