package com.tradetaker.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

/*
CREATE TABLE public.nsebhavcopy (
	id serial4 NOT NULL,
	instrument bpchar(50) NOT NULL,
	symbol bpchar(50) NOT NULL,
	expiry_dt date NOT NULL,
	strike_pr float8 NOT NULL,
	option_typ bpchar(50) NOT NULL,
	"open" float8 NOT NULL,
	high float8 NOT NULL,
	low float8 NOT NULL,
	"close" float8 NOT NULL,
	settle_pr float8 NOT NULL,
	contracts int4 NOT NULL,
	val_inlakh float8 NOT NULL,
	open_int int4 NOT NULL,
	chg_in_oi int4 NOT NULL,
	"timestamp" date NOT NULL,
	CONSTRAINT nsebhavcopy_pkey PRIMARY KEY (id)
);
**/

@Entity
@Setter
@Getter
@Table(name = 'NSEBHAVCOPY')
class NSEBhavCopyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = 'id')
    @JsonProperty(value = 'id')
    private long id
    @Column(name = 'instrument')
    @JsonProperty(value = 'instrument')
    public String instrument
    @Column(name = 'symbol')
    @JsonProperty(value = 'symbol')
    public String symbol
    @Column(name = 'expiry_dt')
    @JsonProperty(value = 'expiry_dt')
    public Date expiryDate
    @Column(name = 'strike_pr')
    @JsonProperty(value = 'strike_pr')
    public Float strikePrice
    @Column(name = 'option_typ')
    @JsonProperty(value = 'option_typ')
    public String optionType
    @Column(name = 'open')
    @JsonProperty(value = 'open')
    public Float open
    @Column(name = 'high')
    @JsonProperty(value = 'high')
    public Float high
    @Column(name = 'low')
    @JsonProperty(value = 'low')
    public Float low
    @Column(name = 'close')
    @JsonProperty(value = 'close')
    public Float close
    @Column(name = 'settle_pr')
    @JsonProperty(value = 'settle_pr')
    public Float settlePrice
    @Column(name = 'contracts')
    @JsonProperty(value = 'contracts')
    public int contracts
    @Column(name = 'val_inlakh')
    @JsonProperty(value = 'val_inlakh')
    public Float valInLakh
    @Column(name = 'open_int')
    @JsonProperty(value = 'open_int')
    public int openIntrest
    @Column(name = 'chg_in_oi')
    @JsonProperty(value = 'chg_in_oi')
    public int changeInOI
    @Column(name = 'timestamp')
    @JsonProperty(value = 'timestamp')
    public Date timestamp
}
