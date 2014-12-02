package com.emostn.publictransit2.model;

public class Stop {
	private Long id;
	private String name;
	private Float latitude;
	private Float longtitude;
	private Long routeId;

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	public Long getRouteId() {
		return routeId;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLongtitude(Float longtitude) {
		this.longtitude = longtitude;
	}

	public Float getLongtitude() {
		return longtitude;
	}
}
