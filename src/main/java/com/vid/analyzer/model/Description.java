package com.vid.analyzer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Description {

        private List<String> tags;
        private List<Caption> captions;

}
