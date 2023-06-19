package pe.edu.utp.articulosapi3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pe.edu.utp.articulosapi3.entity.Articulo;
import pe.edu.utp.articulosapi3.exception.GeneralServiceException;
import pe.edu.utp.articulosapi3.exception.NoDataFoundException;
import pe.edu.utp.articulosapi3.exception.ValidateServiceException;
import pe.edu.utp.articulosapi3.repository.ArticuloRepository;
import pe.edu.utp.articulosapi3.service.ArticuloService;
import pe.edu.utp.articulosapi3.validator.ArticuloValidator;

@Slf4j
@Service
public class ArticuloServiceImpl implements ArticuloService {

	@Autowired
	private ArticuloRepository repository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Articulo> findAll(Pageable page) {
		try {
			return repository.findAll(page).toList();
		}catch(ValidateServiceException | NoDataFoundException e){
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Articulo> findByNombre(String nombre, Pageable page) {
		try {
			return repository.findByNombreContaining(nombre,page);
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
	public Articulo findById(int id) {
		try {
			return repository.findById(id).orElseThrow(()-> new NoDataFoundException("No existe el registro con el ID "+id));
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
	public Articulo save(Articulo articulo) {
		try {
			ArticuloValidator.save(articulo);
			if(repository.findByNombre(articulo.getNombre())!=null) {
				throw new ValidateServiceException("Existe un registro con el nombre "+articulo.getNombre());
			}
			return repository.save(articulo);
		} catch (ValidateServiceException | GeneralServiceException e) {
			log.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional
	public Articulo update(Articulo articulo) {
		try {
			ArticuloValidator.save(articulo);
			Articulo registroD = repository.findByNombre(articulo.getNombre());
			if(registroD !=null && registroD.getId()!= articulo.getId()) {
				throw new ValidateServiceException("Existe un registro con ese nombre "+articulo.getNombre());
			}
			Articulo registro=repository.findById(articulo.getId())
					.orElseThrow(()->new NoDataFoundException("No existe el registro con el ID "+articulo.getId()));
			registro.setNombre(articulo.getNombre());
			registro.setPrecio(articulo.getPrecio());
			return repository.save(registro);
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		} 
	}

	@Override
	@Transactional
	public void delete(int id) {
		try {
			Articulo registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con el ID "+id));
			repository.delete(registro);
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}		
	}
}
