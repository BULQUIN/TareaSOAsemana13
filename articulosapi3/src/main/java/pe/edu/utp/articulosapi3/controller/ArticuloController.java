package pe.edu.utp.articulosapi3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import pe.edu.utp.articulosapi3.converter.ArticuloConverter;
import pe.edu.utp.articulosapi3.dto.ArticuloDTO;
import pe.edu.utp.articulosapi3.entity.Articulo;
import pe.edu.utp.articulosapi3.service.ArticuloService;
import pe.edu.utp.articulosapi3.util.WrapperResponse;

@RestController
@RequestMapping("/v1/articulos")
public class ArticuloController {
	@Autowired
	private ArticuloService service;
	
	@Autowired
	private ArticuloConverter converter;
	
	@GetMapping
	public ResponseEntity<List<ArticuloDTO>> findAll(
			@RequestParam(value="nombre", required = false, defaultValue = "") String nombre,
			@RequestParam(value="offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit",required = false,defaultValue = "5") int pageSize
			){
		
		Pageable page=PageRequest.of(pageNumber,pageSize);
		List<Articulo> articulos;
		
		if(nombre==null) {
			articulos=service.findAll(page);
		}else {
			articulos=service.findByNombre(nombre, page);
		}
		List<ArticuloDTO> articulosDTO=converter.fromEntity(articulos);
		
		return new WrapperResponse(true,"success",articulosDTO).createResponse(HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<WrapperResponse<ArticuloDTO>> findById(@PathVariable("id") int id){
		Articulo registro=service.findById(id);
		ArticuloDTO registroDTO=converter.fromEntity(registro);
		return new WrapperResponse(true,"success",registroDTO).createResponse(HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<ArticuloDTO> create(@RequestBody ArticuloDTO articuloDTO){
		Articulo registro=service.save(converter.fromDTO(articuloDTO));
		return new WrapperResponse(true,"success",converter.fromEntity(registro)).createResponse(HttpStatus.CREATED);
	}
	@PutMapping(value="/{id}")
	public ResponseEntity<ArticuloDTO> update(@PathVariable("id") int id, @RequestBody ArticuloDTO articuloDTO){
		Articulo registro=service.update(converter.fromDTO(articuloDTO));
		return new WrapperResponse(true, "success",converter.fromEntity(registro)).createResponse(HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Articulo> delete(@PathVariable("id") int id){
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}
	
}