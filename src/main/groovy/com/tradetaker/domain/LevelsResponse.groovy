package com.tradetaker.domain

import com.fasterxml.jackson.annotation.JsonProperty

class LevelsResponse {
    @JsonProperty(value = 'symbol')
    public String symbol

    @JsonProperty(value = 'support3')
    public String support3

    @JsonProperty(value = 'support2')
    public String support2

    @JsonProperty(value = 'support1')
    public String support1
    
    @JsonProperty(value = 'resistance1')
    public String resistance1

    @JsonProperty(value = 'resistance2')
    public String resistance2

    @JsonProperty(value = 'resistance3')
    public String resistance3

}
