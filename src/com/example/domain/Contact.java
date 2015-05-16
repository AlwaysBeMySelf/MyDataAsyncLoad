package com.example.domain;

public class Contact {
	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", path=" + path + "]";
	}

	private int id;
	private String name;
	private String path;

	public Contact(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}

	public Contact() {
		// TODO Auto-generated constructor stub
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
