package com.app.book.user;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.book.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {
	
	@Id
	@GeneratedValue
	private Long id;
	private String firstname;
	private String lastname;
	private LocalDate dateOfBirth;
	@Column(unique=true)
	private String email;
	private String password;
	private boolean accountLocked;
	private boolean enabled;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;
	
	// PARA QUE LAS DOS VARIABLES FUNCIONEN DE LA FORMA ESPERADA HAY QUE AÑADIR LA ANOTACION @EnableJpaAuditing EN application.java
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;

	@Override
	public String getName() {
		// ES EL NOMBRE UNICO DEL USUARIO
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// RETORNA EL CAMPO DE CONTRASEÑA
		return password;
	}

	@Override
	public String getUsername() {
		// RETORNA EL MISMO MAIL QUE getName()
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// COMPRUEBA SI LA CUENTA HA CQUEDADO BLOQUEADA
		return !accountLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// NOS DA INFORMACION SOBRE EL ESTADO DE LAS CREDENCIALES DEL USUARIO
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		// NOS DEVUELVE UN BOOLEAN ESPECIFICANDO SI ESTA LA CUENTA ACTIVA
		return enabled;
	}
	
	private String fullName() {
		return firstname + " " + lastname;
	}
}
