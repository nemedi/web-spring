package demo;

public class CityModel {

	private int id;
	private String name;
	private String district;
	private int inhabitants;
	
	public CityModel(String[] values) {
		id = Integer.parseInt(values[0]);
		name = values[1];
		district = values[2];
		inhabitants = Integer.parseInt(values[3]);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDistrict() {
		return district;
	}

	public int getInhabitants() {
		return inhabitants;
	}
	
}
