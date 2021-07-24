package com.example.tweet.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.tweet.repositary.RegisterRepositary;
import com.example.tweet.model.Register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import java.util.Optional;

@RestController
public class TweetController {
	
	@Autowired
	RegisterRepositary registerRepo;
	
@PostMapping(value="api/v1.0/tweets/register")
public ResponseEntity<String> register(@RequestBody Register newRegister) {
	ResponseEntity<String> response = null;
	try {
		registerRepo.save(newRegister);
		response = new ResponseEntity<String>("sucessfully created",HttpStatus.OK);
	}
	catch (BadRequest ex) {
		response = new ResponseEntity<String>("please enter valid input",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	catch(Exception e) {
		response = new ResponseEntity<String>("The creation of rows was failed due to server issues",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return response;
}

@GetMapping(value="api/v1.0/tweets/login")
public ResponseEntity<String> login(@RequestBody String id){
	ResponseEntity<String> response = null;
	System.out.print(id);
	Optional<Register> list_of_user_data = registerRepo.findById(id);
	System.out.print(list_of_user_data.get());
	response = new ResponseEntity<String>("sucessfully created",HttpStatus.OK);
	return response;
}

}
