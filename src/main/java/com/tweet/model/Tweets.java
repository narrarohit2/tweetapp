package com.tweet.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;

import java.util.List;
import java.util.UUID;
@Table("tweets")
public class Tweets {
	
	@PrimaryKey
	@Column("tweet_id")
	public UUID tweet_id;
	
	@Column("user_id")
	public String user_id;
	
	@Column("likes_of_tweet")
	public List<String> likes_of_tweet;
	
	@Column("comments_to_tweet")
	public List<String> comments_to_tweet;
	
	@Column("description")
	public String description;

	public UUID getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(UUID tweet_id) {
		this.tweet_id = tweet_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<String> getLikes_of_tweet() {
		return likes_of_tweet;
	}

	public void setLikes_of_tweet(List<String> likes_of_tweet) {
		this.likes_of_tweet = likes_of_tweet;
	}

	public List<String> getComments_to_tweet() {
		return comments_to_tweet;
	}

	public void setComments_to_tweet(List<String> comments_to_tweet) {
		this.comments_to_tweet = comments_to_tweet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	

}
