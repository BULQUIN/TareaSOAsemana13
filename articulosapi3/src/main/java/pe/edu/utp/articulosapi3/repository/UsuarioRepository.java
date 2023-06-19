package pe.edu.utp.articulosapi3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.utp.articulosapi3.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	List<Usuario> findByEmailContaining(String email, Pageable page);
	public Optional<Usuario> findByEmail(String email);
}
