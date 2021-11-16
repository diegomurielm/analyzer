package com.vid.analyzer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.vid.analyzer.exception.AnalyzeException;
import com.vid.analyzer.integration.AnalyzeRestTemplate;
import com.vid.analyzer.model.AnalyzeResponse;
import com.vid.analyzer.model.Caption;
import com.vid.analyzer.model.Caras;
import com.vid.analyzer.model.Description;
import com.vid.analyzer.service.AnalyzeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
class AnalyzeServiceImplTest {

	private static AnalyzeServiceImpl service;
	
	@MockBean
	private static AnalyzeRestTemplate mockTemplate;
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		
		MockitoAnnotations.openMocks(AnalyzeServiceImplTest.class);
		mockTemplate = mock(AnalyzeRestTemplate.class);
			service = new AnalyzeServiceImpl(mockTemplate);
	}

	@Test
	void analisisReturnOk() throws AnalyzeException {
		
		List<String> imagenes = new ArrayList<String>();
		imagenes.add("Imagen1");
		imagenes.add("Imagen2");
		
		List<Caras> caras1 = new ArrayList<Caras>();
		caras1.add(Caras.builder().age(10).build());
		
		List<Caras> caras2 = new ArrayList<Caras>();
		caras2.add(Caras.builder().age(12).build());
		
		Description description1 = new Description(Collections.singletonList("Tag1"),Collections.singletonList(new Caption()));
		Description description2 = new Description(Collections.singletonList("Tag2"),Collections.singletonList(new Caption()));
		
		when(mockTemplate.llamadaFaces("Imagen1")).thenReturn(caras1);
		when(mockTemplate.llamadaFaces("Imagen2")).thenReturn(caras2);
		when(mockTemplate.llamadaDescription("Imagen1")).thenReturn(description1);
		when(mockTemplate.llamadaDescription("Imagen2")).thenReturn(description2);
		
		AnalyzeResponse response = service.analisis(imagenes);
		
		assertEquals(2, response.getResponse().size());
		assertEquals(caras1, response.getResponse().get(0).getFaces());
		assertEquals(caras2, response.getResponse().get(1).getFaces());
		assertEquals(description1, response.getResponse().get(0).getDescription());
		assertEquals(description2, response.getResponse().get(1).getDescription());
		
	}

	@Test
	void analisisReturnBadRequest() {
		
		List<String> imagenes = new ArrayList<String>();
		imagenes.add("Imagen1");
				
		try {
			service.analisis(imagenes);
			fail();
		} catch (AnalyzeException e) {
			// TODO Auto-generated catch block
			assertEquals("El número mínimo de imagenes es 2.", e.getMessage());
		}
	}

	@Test
	void analisisReturnErrorImagen() throws AnalyzeException {
		
		List<String> imagenes = new ArrayList<String>();
		imagenes.add("Imagen1");
		imagenes.add("Imagen2");
		
		List<Caras> caras2 = new ArrayList<Caras>();
		caras2.add(Caras.builder().age(12).build());
		
		Description description2 = new Description(Collections.singletonList("Tag2"),Collections.singletonList(new Caption()));
		
		byte[] body = "{\"message\": \"Error Test\"}".getBytes();
		when(mockTemplate.llamadaFaces("Imagen1")).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body, null));

		when(mockTemplate.llamadaFaces("Imagen2")).thenReturn(caras2);
		when(mockTemplate.llamadaDescription("Imagen2")).thenReturn(description2);
		
		AnalyzeResponse response = service.analisis(imagenes);
		
		assertEquals(2, response.getResponse().size());
		assertEquals("Error Test", response.getResponse().get(0).getErrorDescription());
		assertEquals(caras2, response.getResponse().get(1).getFaces());
		assertEquals(description2, response.getResponse().get(1).getDescription());
		
	}

	
}
