package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty

class TriggersRequest {
    @JsonProperty(value = 'stocks')
    public String stocks
    @JsonProperty(value = 'trigger_prices')
    public String trigger_prices
    @JsonProperty(value = 'triggered_at')
    public String triggered_at
    @JsonProperty(value = 'scan_name')
    public String scan_name
    @JsonProperty(value = 'scan_url')
    public String scan_url
    @JsonProperty(value = 'alert_name')
    public String alert_name
    @JsonProperty(value = 'webhook_url')
    public String webhook_url
}
