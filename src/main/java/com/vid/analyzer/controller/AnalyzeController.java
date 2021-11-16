package com.vid.analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vid.analyzer.exception.AnalyzeException;
import com.vid.analyzer.model.AnalyzeRequest;
import com.vid.analyzer.model.AnalyzeResponse;
import com.vid.analyzer.service.AnalyzeService;

@RestController
public class AnalyzeController {
	
	@Autowired
	private AnalyzeService service;

	@GetMapping("/analyze")
	public ResponseEntity<AnalyzeResponse> analyze(@RequestBody AnalyzeRequest request) {

		try {
			return ResponseEntity.ok(service.analisis(request.getImagenes()));
		} catch (AnalyzeException e) {
			
			return new ResponseEntity<>(AnalyzeResponse.builder().errorDescription(e.getMessage()).build(), e.getStatus() == null
					? HttpStatus.INTERNAL_SERVER_ERROR
					: e.getStatus());
		}

	}
}
