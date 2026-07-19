package com.urlshort.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory(
			@Value("${spring.data.redis.host}") String host,
			@Value("${spring.data.redis.port}") int port,
			@Value("${spring.data.redis.password:}") String password,
			@Value("${spring.data.redis.database}") int database,
			@Value("${spring.data.redis.ssl.enabled:false}") boolean sslEnabled) {

		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPort(port);
		if (password != null && !password.isBlank()) {
			config.setPassword(RedisPassword.of(password));
		}
		config.setDatabase(database);

		LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfig =
				LettuceClientConfiguration.builder()
						.commandTimeout(Duration.ofSeconds(5));
		if (sslEnabled) {
			// ElastiCache in-transit encryption often needs this for hostname verification
			clientConfig.useSsl().disablePeerVerification();
		}

		LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig.build());
		factory.setEagerInitialization(false);
		return factory;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
}
