package pe.edu.utp.articulosapi3.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import pe.edu.utp.articulosapi3.entity.Articulo;

public interface ArticuloService {
	public List<Articulo> findAll(Pageable page);
	public List<Articulo> findByNombre(String nombre, Pageable page);
	public Articulo findById(int id);
	public Articulo save(Articulo articulo);
	public Articulo update(Articulo articulo);
	public void delete(int id);
}
