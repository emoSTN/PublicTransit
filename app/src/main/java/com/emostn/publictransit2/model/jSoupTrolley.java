package com.emostn.publictransit2.model;

public class jSoupTrolley
{
	private Long id;
	private String name;
	private String url;
	private String html;

	public jSoupTrolley(String name, String url, String html) {
		this.name = name;
		this.url = url;
		this.html = html;
	}

	public jSoupTrolley(){}
	
	
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

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" ");
		sb.append(url).append(" \n");
		sb.append(html).append(" ");
		
		return sb.toString();
	}
	
	
}
