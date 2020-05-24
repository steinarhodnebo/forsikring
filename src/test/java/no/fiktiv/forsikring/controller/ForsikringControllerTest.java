package no.fiktiv.forsikring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.fiktiv.forsikring.domain.Avtale;
import no.fiktiv.forsikring.domain.AvtaleSendt;
import no.fiktiv.forsikring.domain.Kunde;

@RunWith(SpringRunner.class)
@WebMvcTest(ForsikringController.class)
public class ForsikringControllerTest {

	ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void testOpprettAvtale() throws Exception {
		Kunde kunde = new Kunde();
		kunde.setNavn("Test");
		kunde.setId(123L);
		//OpprettKunde
		when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Kunde.class))).thenReturn(kunde);

		Avtale avtale = new Avtale();
		avtale.setId(456L);
		//opprettAvtale
		when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Avtale.class))).thenReturn(avtale);

		//sendAvtale
		when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(AvtaleSendt.class))).thenReturn(AvtaleSendt.SENDT);

		//endreStatusAvtale
		avtale.setAvtaleSendt(AvtaleSendt.SENDT);
		ResponseEntity<Avtale> myEntity = new ResponseEntity<Avtale>(avtale, HttpStatus.ACCEPTED);
		when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
				eq(Avtale.class))).thenReturn(myEntity);

		String json = objectMapper.writeValueAsString(kunde);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/forsikring/opprett-avtale").contentType("application/json")
				.content(json.getBytes())).andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		Avtale resultatAvtale = objectMapper.readValue(content, Avtale.class);
		assertEquals(AvtaleSendt.SENDT, resultatAvtale.getAvtaleSendt());
	}

	@Test
	public void testOpprettAvtaleFeil() throws Exception {
		Kunde kunde = new Kunde();
		kunde.setNavn("Test");
		kunde.setId(123L);
		//OpprettKunde
		when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Kunde.class))).thenReturn(kunde);

		Avtale avtale = new Avtale();
		avtale.setId(456L);
		//opprettAvtale
		when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Avtale.class))).thenReturn(avtale);

		//sendAvtale
		when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(AvtaleSendt.class))).thenReturn(AvtaleSendt.SENDT);

		//endreStatusAvtale
		avtale.setAvtaleSendt(AvtaleSendt.SENDT);
		when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
				eq(Avtale.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved kall av endreStatusAvtale"));

		String json = objectMapper.writeValueAsString(kunde);
		mockMvc.perform(MockMvcRequestBuilders.post("/forsikring/opprett-avtale").contentType("application/json")
				.content(json.getBytes())).andExpect(status().is5xxServerError()).andReturn();
	}
}
