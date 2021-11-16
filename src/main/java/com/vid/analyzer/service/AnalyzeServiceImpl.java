package com.vid.analyzer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vid.analyzer.exception.AnalyzeException;
import com.vid.analyzer.integration.AnalyzeRestTemplate;
import com.vid.analyzer.model.Analyze;
import com.vid.analyzer.model.AnalyzeResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyzeServiceImpl implements AnalyzeService {

	
	AnalyzeRestTemplate restTemplate;
	
	@Autowired
	public AnalyzeServiceImpl(AnalyzeRestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public AnalyzeResponse analisis(List<String> imagenes) throws AnalyzeException {
		
		if(imagenes.size()<2) {
			throw new AnalyzeException("El número mínimo de imagenes es 2.", HttpStatus.BAD_REQUEST);
		}
		List<Analyze> response = new ArrayList<>();
		for(String imagen : imagenes) {
			Analyze analyze = new Analyze();
			//Llamada a faces
			try {
				analyze.setFaces(restTemplate.llamadaFaces(imagen));
				if(analyze.getFaces().size()>0) {
					//Llamada Description
					analyze.setDescription(restTemplate.llamadaDescription(imagen));
				}
			}catch (HttpClientErrorException e) {
	        	ObjectMapper mapper = new ObjectMapper();
	        	JsonNode root;
	        	String error = "";
				try {
					root = mapper.readTree(e.getResponseBodyAsString());
		            if(root.has("message")){
		        		error = root.get("message").asText().trim();
		        	}
				} catch (JsonProcessingException e1) {
					log.error("JsonProcessingException : {}", e1);
				}

				analyze.setErrorDescription(error);
			}
			response.add(analyze);
		}		
		return AnalyzeResponse.builder().response(response).build();
	}

}
