package com.urlshort.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory(
			@Value("${spring.data.redis.host}") String host,
			@Value("${spring.data.redis.port}") int port,
			@Value("${spring.data.redis.password}") String password,
			@Value("${spring.data.redis.database}") int database) {

		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPort(port);
		config.setPassword(RedisPassword.of(password));
		config.setDatabase(database);

		return new LettuceConnectionFactory(config);
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
}
