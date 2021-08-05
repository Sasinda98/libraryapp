package com.gsr.library.libraryapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryappApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryappApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
