package com.microservice.stockdatastreamer.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockSymbol {

    private String symbol;
    private String companyName;


}
