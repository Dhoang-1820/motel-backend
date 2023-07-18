package com.petproject.motelservice.exeption;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ErrorMessage {
	
  private int statusCode;
  
  private Date timestamp;
  
  private String message;
  
  private String description;

}