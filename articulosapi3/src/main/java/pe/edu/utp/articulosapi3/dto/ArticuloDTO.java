package pe.edu.utp.articulosapi3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloDTO {
	private int id;
	private String nombre;
	private Double precio;
}