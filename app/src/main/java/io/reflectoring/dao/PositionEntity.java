package io.reflectoring.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.reflectoring.model.Position;

@Entity(name = "POSITION")
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"name", "location"})
	})
public class PositionEntity {


	public PositionEntity(Position position){
		super();
		this.name = position.getName();
		this.location = position.getLocation();
	}
	
	public PositionEntity() {
		
	}
	
	public PositionEntity(String name, String location) {
		super();
		this.name = name;
		this.location = location;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String location;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	
	
}
