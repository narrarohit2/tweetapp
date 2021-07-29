package com.tweet.repositary;

import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import com.tweet.model.Register;
public interface RegisterRepositary extends CassandraRepository<Register, String>{
	

}
