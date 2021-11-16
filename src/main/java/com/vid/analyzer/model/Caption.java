package com.vid.analyzer.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Caption {

        private String text;
        private BigDecimal confidence;

}
