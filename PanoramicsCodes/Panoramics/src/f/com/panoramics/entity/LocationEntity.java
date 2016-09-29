package f.com.panoramics.entity;

import fatty.library.sqlite.core.ANNOTATION_ID;
import fatty.library.sqlite.core.ANNOTATION_TABLE;

@ANNOTATION_TABLE(name = "panoramics_location_table")
public class LocationEntity {

	@ANNOTATION_ID
	private int id;
	private String name;
	private String url;

	private double lat;
	private double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
