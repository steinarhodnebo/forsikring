package no.fiktiv.forsikring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import no.fiktiv.forsikring.domain.Avtale;
import no.fiktiv.forsikring.domain.AvtaleSendt;
import no.fiktiv.forsikring.domain.Kunde;
import no.fiktiv.forsikring.service.BrevService;
import no.fiktiv.forsikring.service.FagsystemService;

@Controller
@RequestMapping("forsikring")
public class ForsikringController {

	@Autowired
	private FagsystemService fagsystemService;

	@Autowired
	private BrevService brevService;

	@PostMapping(path = "/opprett-avtale", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Avtale> opprettAvtale(@RequestBody Kunde kundeObj) {
		try {
			Kunde nyKunde = fagsystemService.opprettKunde(kundeObj);
			Avtale avtale = fagsystemService.opprettAvtale(nyKunde);
			AvtaleSendt status = brevService.sendAvtale(avtale);
			return new ResponseEntity<>(fagsystemService.endreStatusAvtale(avtale, status), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: Bedre håndering av feilet transaksjon, spesielt endreStatusAvtale
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
