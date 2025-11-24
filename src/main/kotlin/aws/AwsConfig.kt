package com.example.aws

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region

@Configuration
class AwsConfig {

    @Bean
    fun awsRegion(): Region {
        return Region.AP_NORTHEAST_2
    }

    @Bean
    fun awsCredentialsProvider() =
        EnvironmentVariableCredentialsProvider.create()
}
