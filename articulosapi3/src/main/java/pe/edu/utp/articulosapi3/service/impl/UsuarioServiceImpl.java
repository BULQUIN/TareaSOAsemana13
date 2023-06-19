package pe.edu.utp.articulosapi3.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import pe.edu.utp.articulosapi3.converter.UsuarioConverter;
import pe.edu.utp.articulosapi3.dto.LoginRequestDTO;
import pe.edu.utp.articulosapi3.dto.LoginResponseDTO;
import pe.edu.utp.articulosapi3.entity.Usuario;
import pe.edu.utp.articulosapi3.exception.GeneralServiceException;
import pe.edu.utp.articulosapi3.exception.NoDataFoundException;
import pe.edu.utp.articulosapi3.exception.ValidateServiceException;
import pe.edu.utp.articulosapi3.repository.UsuarioRepository;
import pe.edu.utp.articulosapi3.security.JwtService;
import pe.edu.utp.articulosapi3.service.UsuarioService;
import pe.edu.utp.articulosapi3.validator.UsuarioValidator;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService{
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsuarioConverter converter;
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll(Pageable page) {
		try {
			return repository.findAll(page).toList();
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findByEmail(String email, Pageable page) {
		try {
			return repository.findByEmailContaining(email, page);
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findById(int id) {
		try {
			return repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe el registro con el ID "+id));
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		try {
			UsuarioValidator.save(usuario);
			Optional<Usuario> reg=repository.findByEmail(usuario.getEmail());
			if(reg.isPresent()) {
				throw new ValidateServiceException("Ya existe un registro con el email "+usuario.getEmail());
			}
			String passEncode=encoder.encode(usuario.getPassword());
			usuario.setPassword(passEncode);
			usuario.setActivo(true);
			Usuario registro = repository.save(usuario);
			return registro;
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Usuario update(Usuario usuario) {
		try {
			UsuarioValidator.save(usuario);
			Usuario registroD=repository.findByEmail(usuario.getEmail()).orElseThrow(()->new NoDataFoundException("No existe el registro con el email "+usuario.getEmail()));
			if(registroD !=null && registroD.getId()!= usuario.getId()) {
				throw new ValidateServiceException("Ya existe un registro con el email "+usuario.getEmail());
			}
			Usuario registro=repository.findById(usuario.getId()).orElseThrow(()->new NoDataFoundException("No existe el registro con el ID "+usuario.getId()));
			registro.setEmail(usuario.getEmail());
			String passEncode=encoder.encode(usuario.getPassword());
			registro.setPassword(passEncode);
			registro.setRol(usuario.getRol());
			repository.save(registro);
			return registro;
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void delete(int id) {
		try {
			Usuario registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe el registro con el ID "+id));
			repository.delete(registro);
		} catch (ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
		
	}

	@Override
	public LoginResponseDTO login(LoginRequestDTO request) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			var usuario=repository.findByEmail(request.getEmail()).orElseThrow();
			var jwtToken=jwtService.generateToken(usuario);
			return new LoginResponseDTO(converter.fromEntity(usuario),jwtToken);
		} catch (JwtException e) {
			log.info(e.getMessage(),e);
			throw new ValidateServiceException(e.getMessage());
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			throw new ValidateServiceException(e.getMessage());
		}
	}
	
}
