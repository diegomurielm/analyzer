package com.vid.analyzer.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vid.analyzer.model.Analyze;
import com.vid.analyzer.model.Caras;
import com.vid.analyzer.model.Description;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Service
@Slf4j
public class AnalyzeRestTemplateImpl implements AnalyzeRestTemplate{
	
	@Autowired
	RestTemplate restTemplate;
	
    private static final String url = "https://microsoft-computer-vision3.p.rapidapi.com/analyze";
	private static final String key = "0279168543mshaf2b3872be2ed39p10e5a9jsn23762b9061d6";

	@Override
	public List<Caras> llamadaFaces(String imagen) {
	
		String requestData = buildRequestData(imagen);
        log.debug("Method : POST, TOKEN, Prestamos requestData = {}", requestData);
        
        StringBuilder urlQuery = new StringBuilder();
        urlQuery.append(url)
        .append("?language={language}&")
        .append("descriptionExclude={descriptionExclude}&")
        .append("visualFeatures={visualFeatures}&")
        .append("details={details}");

        HttpEntity<String> entity = createEntity(requestData);

        ResponseEntity<Analyze> response = restTemplate.exchange(urlQuery.toString(), HttpMethod.POST, entity,
        		Analyze.class, crearQueryValues("faces"));
		
        return response.getBody().getFaces();
	}

	@Override
	public Description llamadaDescription(String imagen) {
		String requestData = buildRequestData(imagen);
        log.debug("Method : POST, TOKEN, Prestamos requestData = {}", requestData);
        
        StringBuilder urlQuery = new StringBuilder();
        urlQuery.append(url)
        .append("?language={language}&")
        .append("descriptionExclude={descriptionExclude}&")
        .append("visualFeatures={visualFeatures}&")
        .append("details={details}");

        HttpEntity<String> entity = createEntity(requestData);

        ResponseEntity<Analyze> response = restTemplate.exchange(urlQuery.toString(), HttpMethod.POST, entity,
        		Analyze.class, crearQueryValues("Description"));

//		AnalyzeOutput out = restTemplate.exchange(url, HttpMethod.POST, AnalyzeOutput.class);
        
        return response.getBody().getDescription();
	}
	
    private String buildRequestData(String imagen) {
    	
    	JSONObject requestData = new JSONObject();
    	
    	requestData.put("url", imagen);
    	
        return requestData.toString();
    }

    private Map<String, String> crearQueryValues(String visualFeature) {
    	
        Map<String, String> queryValuesMap = new HashMap<String, String>();
        queryValuesMap.put("language", "es");
        queryValuesMap.put("descriptionExclude", "");
        queryValuesMap.put("visualFeatures", visualFeature);
        queryValuesMap.put("details", "");
        
        return queryValuesMap;
    	
    }
    
    private HttpEntity<String> createEntity(String request){
        HttpHeaders headers = new HttpHeaders();

        // Seteamos las cabeceras para la petición HTTP:
        //accept
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //Content Type
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Credentials
        headers.set("x-rapidapi-key", key);
//        headers.set("", );
        //creamos una httpEntity usando los headers creados y la request
        return new HttpEntity<>(request, headers);

    }
}
