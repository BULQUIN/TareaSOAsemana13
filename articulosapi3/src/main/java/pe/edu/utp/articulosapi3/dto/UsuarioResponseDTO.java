package pe.edu.utp.articulosapi3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
	private int id;
	private String email;
	private String rol;
	private boolean activo;
}
