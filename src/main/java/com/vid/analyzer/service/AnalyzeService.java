package com.vid.analyzer.service;

import java.util.List;

import com.vid.analyzer.exception.AnalyzeException;
import com.vid.analyzer.model.AnalyzeResponse;

public interface AnalyzeService {

	public AnalyzeResponse analisis(List<String> imagenes) throws AnalyzeException;
}
