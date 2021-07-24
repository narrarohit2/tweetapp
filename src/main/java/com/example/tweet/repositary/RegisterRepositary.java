package com.example.tweet.repositary;

import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import com.example.tweet.model.Register;
public interface RegisterRepositary extends CassandraRepository<Register, String>{
	

}
