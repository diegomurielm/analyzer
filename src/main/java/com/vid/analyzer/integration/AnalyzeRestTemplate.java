package com.vid.analyzer.integration;

import java.util.List;

import com.vid.analyzer.model.Caras;
import com.vid.analyzer.model.Description;

public interface AnalyzeRestTemplate {

	List<Caras> llamadaFaces(String imagen);

	Description llamadaDescription(String imagen);

}
