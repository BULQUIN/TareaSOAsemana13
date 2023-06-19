package pe.edu.utp.articulosapi3.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import pe.edu.utp.articulosapi3.dto.LoginRequestDTO;
import pe.edu.utp.articulosapi3.dto.LoginResponseDTO;
import pe.edu.utp.articulosapi3.entity.Usuario;

public interface UsuarioService {
	public List<Usuario> findAll(Pageable page);
	public List<Usuario> findByEmail(String email, Pageable page);
	public Usuario findById(int id);
	public Usuario save(Usuario usuario);
	public Usuario update(Usuario usuario);
	public void delete(int id);
	public LoginResponseDTO login(LoginRequestDTO request);
}
