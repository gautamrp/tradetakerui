package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty

class OptionLevelsResponse {
    @JsonProperty(value = 'instrument')
    public String instrument

    @JsonProperty(value = 'symbol')
    public String symbol

    @JsonProperty(value = 'expiry_dt')
    public String expiryDate

    @JsonProperty(value = 'strike_pr')
    public Float strikePrice

    @JsonProperty(value = 'option_typ')
    public String optionType

    @JsonProperty(value = 'open')
    public Float open

    @JsonProperty(value = 'high')
    public Float high

    @JsonProperty(value = 'low')
    public Float low

    @JsonProperty(value = 'close')
    public Float close

    @JsonProperty(value = 'settle_pr')
    public Float settlePrice

    @JsonProperty(value = 'contracts')
    public int contracts

    @JsonProperty(value = 'val_inlakh')
    public Float valInLakh

    @JsonProperty(value = 'open_int')
    public int openIntrest

    @JsonProperty(value = 'chg_in_oi')
    public int changeInOI

    @JsonProperty(value = 'timestamp')
    public String timestamp


}
