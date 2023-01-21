package io.reflectoring.dao;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.reflectoring.model.Client;

@Entity(name = "CLIENT")
public class ClientEntity {

	public ClientEntity(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	public ClientEntity() {}
	
	public ClientEntity(Client client) {
		super();
		this.name = client.getName();
		this.email = client.getEmail();
	}
	
	@Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("id: ").append(id).append(", ");	    
	    sb.append("name: ").append(name).append(", ");
	    sb.append("email: ").append(email).append(", ");
	    sb.append("uuid: ").append(uuid);
	    return sb.toString();
	  }

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	private String name;
	@Column(unique=true)
	private String email;
	@Column(unique=true)
	private UUID uuid = UUID.randomUUID();
	
	

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
}