package com.user.user;

import jakarta.persistence.*;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private long id;

	@Basic(optional = false)
	@Column(unique=true)
	private String email;

	@Basic(optional = false)
	@Column
	private String password;
	
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	@Column
	private Role role;
	
	@Basic(optional = true)
	@Transient
	private String environment;

	public Users() {

	}
	
	public Users(long id, String email, String password, Role role, String enviroment) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
		this.environment = enviroment;
	}
	public Users(String email, String password, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

}
