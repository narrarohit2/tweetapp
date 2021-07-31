package com.tweet.repositary;

import org.springframework.data.cassandra.repository.CassandraRepository;
import com.tweet.model.Tweets;
import java.util.UUID;

public interface TweetsRepositary extends CassandraRepository<Tweets,UUID>{
	

}
