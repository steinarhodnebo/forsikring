package no.fiktiv.forsikring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import no.fiktiv.forsikring.domain.Avtale;
import no.fiktiv.forsikring.domain.AvtaleSendt;

@Service
public class BrevService {

	@Value("${brevtjenesteUrl:http://localhost:8088}")
	private String brevtjenesteUrl;

	@Autowired
	private RestTemplate restTemplate;

	public AvtaleSendt sendAvtale(Avtale avtale) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Avtale> request = new HttpEntity<>(avtale, headers);
		return restTemplate.postForObject(brevtjenesteUrl + "/send-avtale", request, AvtaleSendt.class);
	}
}
