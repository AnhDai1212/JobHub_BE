package com.daita.datn.repositories;

import com.daita.datn.models.entities.auth.RedisToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {

}
