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
public class UsuarioRequestDTO {
	private String email;
	private String password;
	private String rol;
}
