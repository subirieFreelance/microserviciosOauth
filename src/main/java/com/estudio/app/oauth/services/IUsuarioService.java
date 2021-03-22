package com.estudio.app.oauth.services;

import com.estudio.app.commons.usuarios.models.entities.Usuario;

public interface IUsuarioService {
	public Usuario findByUserName(String username);
}
