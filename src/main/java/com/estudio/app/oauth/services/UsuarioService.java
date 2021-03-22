package com.estudio.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.estudio.app.commons.usuarios.models.entities.Usuario;
import com.estudio.app.oauth.clients.UsuarioFeignClient;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService{

	private Logger log = LoggerFactory.getLogger(UsuarioService.class);
	
	@Autowired
	private UsuarioFeignClient client;
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Usuario usuario = client.getByName(name);
		if(usuario==null)
		{
			log.error("Error en el login, no existe usuario '"+name+"' en el sistema");
			throw new UsernameNotFoundException("Error en el login, no existe usuario '"+name+"' en el sistema");
		}
		List<GrantedAuthority> authorities = usuario.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
		log.info("Usuario autenticado:" + name);
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getActivo().equals("S")?true:false, true, true, true, authorities);
	}

	@Override
	public Usuario findByUserName(String username) {
		return client.getByName(username);
	}
}