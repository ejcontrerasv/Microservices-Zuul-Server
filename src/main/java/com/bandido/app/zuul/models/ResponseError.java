package com.bandido.app.zuul.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError implements Serializable{

	private static final long serialVersionUID = -4664348484843780119L;
	
	private String mensaje;
	private String status;
	private int codigo;

}
