package com.example.demo.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

	@Value("${aws.credentials.accessKey}")
	private String accessKey;

	@Value("${aws.credentials.secretKey}")
	private String secretAccessKey;

	@Value("${aws.region.static}")
	private String region;

	@Bean
	public S3Client s3Client() {
		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretAccessKey);
		return S3Client.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
			.build();
	}

	@Bean
	public S3Presigner s3Presigner() {
		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretAccessKey);
		return S3Presigner.builder()
			.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
			.region(Region.of(region))
			.build();
	}
}
