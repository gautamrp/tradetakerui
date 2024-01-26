package com.tradetaker.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Entity
@Setter
@Getter
@Table(name = 'TRIGGERS')
class TriggerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'id')
    @JsonProperty(value = 'id')
    private long id
    @Column(name = 'stock')
    @JsonProperty(value = 'stock')
    public String stock
    @Column(name = 'trigger_price')
    @JsonProperty(value = 'trigger_price')
    public Float triggerPrice
    @Column(name = 'triggered_at')
    @JsonProperty(value = 'triggered_at')
    public String triggeredAt
    @Column(name = 'scan_name')
    @JsonProperty(value = 'scan_name')
    public String scanName
    @Column(name = 'scan_url')
    @JsonProperty(value = 'scan_url')
    public String scanURL
    @Column(name = 'alert_name')
    @JsonProperty(value = 'alert_name')
    public String alertName
    @Column(name = 'triggered_date')
    @JsonProperty(value = 'triggered_date')
    public String triggeredDate
}
