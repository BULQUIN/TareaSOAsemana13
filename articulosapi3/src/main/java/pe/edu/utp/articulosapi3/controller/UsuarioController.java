package pe.edu.utp.articulosapi3.controller;

import java.util.List;

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

import pe.edu.utp.articulosapi3.converter.UsuarioConverter;
import pe.edu.utp.articulosapi3.dto.LoginRequestDTO;
import pe.edu.utp.articulosapi3.dto.LoginResponseDTO;
import pe.edu.utp.articulosapi3.dto.UsuarioRequestDTO;
import pe.edu.utp.articulosapi3.dto.UsuarioResponseDTO;
import pe.edu.utp.articulosapi3.entity.Usuario;
import pe.edu.utp.articulosapi3.service.UsuarioService;
import pe.edu.utp.articulosapi3.util.WrapperResponse;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private UsuarioConverter converter;
	
	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> findAll(
			@RequestParam(value="email",required=false) String email,
			@RequestParam(value="offset",required=false,defaultValue = "0") int pageNumber,
			@RequestParam(value="limit",required=false,defaultValue = "5") int pageSize
			){
		Pageable pagina=PageRequest.of(pageNumber, pageSize);
		List<Usuario> registros;
		if(email==null) {
			registros=service.findAll(pagina);
		}else {
			registros=service.findByEmail(email, pagina);
		}
		List<UsuarioResponseDTO> registrosDTO=converter.fromEntity(registros);
		return new WrapperResponse(true,"success",registrosDTO).createResponse(HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<WrapperResponse<UsuarioResponseDTO>> findById(@PathVariable("id") int id){
		Usuario registro=service.findById(id);
		UsuarioResponseDTO registroDTO= converter.fromEntity(registro);
		if(registro==null) {
			return ResponseEntity.notFound().build();
		}
		return new WrapperResponse(true,"success",registroDTO).createResponse(HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO usuario){
		Usuario registro=service.save(converter.registro(usuario));
		return new WrapperResponse(true,"success",converter.fromEntity(registro)).createResponse(HttpStatus.CREATED);	
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UsuarioResponseDTO> update(@PathVariable("id") int id,@RequestBody UsuarioRequestDTO usuario){
		Usuario registro=service.update(converter.registro(usuario));
		if(registro==null) {
			return ResponseEntity.notFound().build();
		}
		return new WrapperResponse(true,"success",converter.fromEntity(registro)).createResponse(HttpStatus.OK);	
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<UsuarioRequestDTO> delete(@PathVariable("id") int id){
		service.delete(id);
		return new WrapperResponse(true,"success",null).createResponse(HttpStatus.OK);	
	}
	
	@PostMapping(value="login")
	public ResponseEntity<WrapperResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request){
		LoginResponseDTO response=service.login(request);
		return new WrapperResponse<>(true,"success",response).createResponse(HttpStatus.OK);
	}
}
