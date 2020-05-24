package no.fiktiv.forsikring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.fiktiv.forsikring.domain.Avtale;
import no.fiktiv.forsikring.domain.AvtaleSendt;
import no.fiktiv.forsikring.domain.Kunde;

@Service
public class FagsystemService {

	@Value("${fakturaUrl:http://localhost:8080}")
	private String fakturaUrl;

	@Autowired
	private RestTemplate restTemplate;

	public Kunde opprettKunde(Kunde kunde) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Kunde> request = new HttpEntity<>(kunde, headers);
		return restTemplate.postForObject(fakturaUrl + "/opprett-kunde", request, Kunde.class);
	}

	public Avtale opprettAvtale(Kunde kunde) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Kunde> request = new HttpEntity<>(kunde, headers);
		return restTemplate.postForObject(fakturaUrl + "/opprett-avtale", request, Avtale.class);
	}

	public Avtale endreStatusAvtale(Avtale avtale, AvtaleSendt avtaleSendt) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(avtaleSendt.name(), headers);
		// Idempotent kall for endring av status på avtale
		HttpEntity<Avtale> response = restTemplate.exchange(fakturaUrl + "/endre-status/" + avtale.getId(),
				HttpMethod.PUT, request, Avtale.class);
		return response.getBody();
	}
}
