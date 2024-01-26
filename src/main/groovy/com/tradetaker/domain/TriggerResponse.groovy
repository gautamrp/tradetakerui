package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.Setter

@Setter
@Getter
class TriggerResponse {
    @JsonProperty(value = 'stock')
    public String stock
    @JsonProperty(value = 'isNifty50')
    public String isNifty50
    @JsonProperty(value = 'trigger_price')
    public Float triggerPrice
    @JsonProperty(value = 'triggered_at')
    public String triggeredAt
    @JsonProperty(value = 'scan_name')
    public String scanName
    @JsonProperty(value = 'scan_url')
    public String scanURL
    @JsonProperty(value = 'alert_name')
    public String alertName
    @JsonProperty(value = 'triggered_date')
    public String triggeredDate
    @JsonProperty(value = 'support')
    public String support
    @JsonProperty(value = 'resistance')
    public String resistance
    @JsonProperty(value = 'calls')
    public List<String> calls
    @JsonProperty(value = 'settle_pr')
    public List<Float> settlePrice
}
