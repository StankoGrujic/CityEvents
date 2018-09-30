package com.szmasters.meetup;

public class Event {
	private String name;
	private String description;
	private String local_time;
	private String local_date;
	private String address;
	private String place;
	public Event(String name, String description, String local_time, String local_date, String address, String place) {
		this.name = name;
		this.description = description;
		this.local_time = local_time;
		this.local_date = local_date;
		this.address = address;
		this.place = place;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getLocal_time() {
		return local_time;
	}
	public String getLocal_date() {
		return local_date;
	}
	public String getAddress() {
		return address;
	}
	public String getPlace() {
		return place;
	}
}
