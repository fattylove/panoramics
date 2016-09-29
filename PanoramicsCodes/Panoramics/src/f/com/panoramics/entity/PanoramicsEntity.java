package f.com.panoramics.entity;

import fatty.library.sqlite.core.ANNOTATION_ID;
import fatty.library.sqlite.core.ANNOTATION_TABLE;


@ANNOTATION_TABLE(name = "panoramics_pano_table")
public class PanoramicsEntity {

	@ANNOTATION_ID
	private int id;
	
	private String imgPath;
	private double lat;
	private double lng;
	private int img_h;
	private int img_w;

	public int getImg_h() {
		return img_h;
	}

	public void setImg_h(int img_h) {
		this.img_h = img_h;
	}

	public int getImg_w() {
		return img_w;
	}

	public void setImg_w(int img_w) {
		this.img_w = img_w;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

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

}
