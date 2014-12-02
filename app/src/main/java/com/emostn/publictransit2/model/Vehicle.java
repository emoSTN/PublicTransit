package com.emostn.publictransit2.model;
import java.util.*;

public class Vehicle {
	private Long id;
	private String type;
	private String name;
	private String routeDescription;
	private List<Stop> stops;

	public Vehicle(){}
	public Vehicle(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	

	public void addStop(Stop route) {
		if(!stops.contains(route)){
			stops.add(route);
		}
	}

	public List<Stop> getStop() {
		return stops;
	}
	
}
