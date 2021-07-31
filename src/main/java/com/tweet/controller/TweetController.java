package com.tweet.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.tweet.model.Register;
import com.tweet.model.Tweets;
import com.tweet.repositary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.*;

@RestController
public class TweetController {
	
	@Autowired
	RegisterRepositary registerRepo;
	
	@Autowired 
	TweetsRepositary tweetsRepo;
	
	
@PostMapping(value="api/v1.0/tweets/register")
public ResponseEntity<String> register(@RequestBody Register newRegister) {
	ResponseEntity<String> response = null;
	try {
		registerRepo.save(newRegister);
		response = new ResponseEntity<String>("sucessfully created",HttpStatus.OK);
	}
	catch (BadRequest ex) {
		response = new ResponseEntity<String>("please enter valid input",HttpStatus.BAD_REQUEST);
	}
	catch(Exception e) {
		response = new ResponseEntity<String>("The creation of rows was failed due to server issues",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return response;
}

@GetMapping(value="api/v1.0/tweets/login")
public ResponseEntity<String> login(@RequestBody List<String> user_details){
	ResponseEntity<String> response = null;
	Optional<Register> list_of_user_data = registerRepo.findById(user_details.get(0));
	if(list_of_user_data.isPresent()) {
	Register user_cred = list_of_user_data.get();
	String user_pwd = user_cred.getPassword();
	if(user_pwd.equals(user_details.get(1))) {
		response = new ResponseEntity<String>("sucessfully created",HttpStatus.OK);
	}
	else
	{
		response = new ResponseEntity<String>("Invalid user name or password",HttpStatus.FORBIDDEN);
	}
	}
	else
	{
		System.out.print("no data");
	}
	return response;
}

@GetMapping(value="api/v1.0/tweets/{loginid}/forgot")
public ResponseEntity<String> forgot_password(@PathVariable("loginid") String id){
	ResponseEntity<String> res = null;
	try {
		List<Register> list_of_ids= registerRepo.findAll();
		List<Register> filtered_list =list_of_ids.stream().filter(p ->p.getLoginid().equals(id)).collect(Collectors.toList());
		List user_cred  = filtered_list.stream().map(li ->li.getPassword()).collect(Collectors.toList());
		String cred = user_cred.get(0).toString();
		res = new ResponseEntity<String>("your password is"+cred,HttpStatus.OK);
	}
	catch (Exception e) {
		res = new ResponseEntity<String>("Invalid loginid",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return res;
}

@GetMapping(value="/api/v1.0/tweets/users/all")
public ResponseEntity<List<String>> get_all_users(){
	ResponseEntity<List<String>> response_obj = null;
	try {
		List<Register> list_of_user_details = registerRepo.findAll();
		List<String> user_ids = list_of_user_details.stream().map(s->s.getLoginid()).collect(Collectors.toList());
		response_obj = new ResponseEntity<List<String>>(user_ids,HttpStatus.OK);
	}
	catch(Exception e) {
		response_obj = new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return response_obj;
}

@PostMapping(value="/api/v1.0/tweets/{username}/add")

public ResponseEntity<String> post_a_tweet(@PathVariable("username") String name,@RequestBody Tweets tweet){
	ResponseEntity<String> res_of_post = null;
	try {
		tweet.setUser_id(name);
		tweetsRepo.save(tweet);	
		res_of_post = new ResponseEntity<String>("Sucessfully posted a tweet",HttpStatus.OK);
	}
	catch (BadRequest ex) {
		res_of_post = new ResponseEntity<String>("please enter valid input",HttpStatus.BAD_REQUEST);
	}
	catch(Exception e) {
		res_of_post = new ResponseEntity<String>("Unable to post tweet due to server issue",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	return res_of_post;
	
}
@PutMapping(value="/api/v1.0/tweets/{username}/update/{id}")
public ResponseEntity<String> update_tweet(@PathVariable("username") String loginid,@PathVariable("id") UUID tweetid,@RequestBody Tweets updatetweet){
	ResponseEntity<String> response_of_update = null;
	try {
		Optional<Tweets> tweet_data = tweetsRepo.findById(tweetid);
		if(tweet_data.isPresent()) {
			Tweets user_tweet_data = tweet_data.get();
			user_tweet_data.setDescription(updatetweet.getDescription());
			tweetsRepo.save(user_tweet_data);
			response_of_update = new ResponseEntity<String>("Suycessfully updated",HttpStatus.OK);
		}
		else {
			response_of_update = new ResponseEntity<String>("Suycessfully update",HttpStatus.OK);
		}
	}
	catch (BadRequest ex) {
		response_of_update  = new ResponseEntity<String>("please enter valid input",HttpStatus.BAD_REQUEST);
	}
	catch(Exception e) {
		response_of_update = new ResponseEntity<String>("Error while updating",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return response_of_update;
	
}

@DeleteMapping(value="/api/v1.0/tweets/{username}/delete/{id}")
public ResponseEntity<String> delete_tweet(@PathVariable("username") String login_id,@PathVariable("id") UUID id_of_tweet){
	ResponseEntity<String> delete_response = null;
	try {
		tweetsRepo.deleteById(id_of_tweet);
		delete_response = new ResponseEntity<String>("Sucessfully deleted the tweet",HttpStatus.OK);
	}
	catch (BadRequest ex) {
		delete_response = new ResponseEntity<String>("please enter valid input request",HttpStatus.BAD_REQUEST);
	}
	catch (Exception e) {
		delete_response = new ResponseEntity<String>("Error while deleting the tweet",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return delete_response;
}

@PutMapping(value="/api/v1.0/tweets/{username}/like/{id}")
public ResponseEntity<String> like_tweet(@PathVariable("username") String login_id,@PathVariable("id") UUID id_of_tweet){
	ResponseEntity<String> like_response = null;
	try {
		Optional<Tweets> tweet_data_of_id = tweetsRepo.findById(id_of_tweet);
		if(tweet_data_of_id.isPresent()) {
			Tweets user_tweet_data_to_like = tweet_data_of_id.get();
			List<String> list_of_like_data;
			if(user_tweet_data_to_like.getLikes_of_tweet() == null)
			{
				List<String> list_of_empty_data = new ArrayList<String>();
				list_of_empty_data.add("liked by"+login_id);	
				user_tweet_data_to_like.setLikes_of_tweet(list_of_empty_data);
			}
			else
			{
				list_of_like_data = user_tweet_data_to_like.getLikes_of_tweet();
				list_of_like_data.add("liked by"+login_id);
				user_tweet_data_to_like.setLikes_of_tweet(list_of_like_data);
			}
			tweetsRepo.save(user_tweet_data_to_like);
			like_response = new ResponseEntity<String>("liked the tweet",HttpStatus.OK);
		}
		else {
			like_response = new ResponseEntity<String>("no tweet was present to like",HttpStatus.OK);
		}
	}
	catch (BadRequest ex) {
		like_response  = new ResponseEntity<String>("please enter valid input",HttpStatus.BAD_REQUEST);
	}
	catch (Exception e) {
		e.printStackTrace();
		like_response = new ResponseEntity<String>("Error while liking the tweet",HttpStatus.INTERNAL_SERVER_ERROR);
}
	return like_response;
}
}
