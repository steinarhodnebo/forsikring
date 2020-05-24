package no.fiktiv.forsikring.domain;

public class Avtale {

	private Long id;
	private String type;
	private AvtaleSendt sendt;
	private Kunde kunde;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AvtaleSendt getAvtaleSendt() {
		return sendt;
	}

	public void setAvtaleSendt(AvtaleSendt sendt) {
		this.sendt = sendt;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

}
