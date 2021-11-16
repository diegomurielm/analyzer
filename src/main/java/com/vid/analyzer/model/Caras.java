package com.vid.analyzer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Caras {
    private int age;
    private String gender;
    private Rectangle faceRectangle;

}
