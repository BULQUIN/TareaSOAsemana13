package pe.edu.utp.articulosapi3.validator;

import pe.edu.utp.articulosapi3.entity.Articulo;
import pe.edu.utp.articulosapi3.exception.ValidateServiceException;

public class ArticuloValidator {
	public static void save(Articulo articulo) {
		if(articulo.getNombre()==null) {
			throw new ValidateServiceException("El nombre es requerido");
		}
		if(articulo.getNombre().length()>100) {
			throw new ValidateServiceException("El nombre es muy extenso, máximo 100 caracteres");
		}
		if(articulo.getPrecio()==null) {
			throw new ValidateServiceException("El precio es requerido");
		}
		if(articulo.getPrecio()<=0) {
			throw new ValidateServiceException("El precio es incorrecto");
		}
	}
}
