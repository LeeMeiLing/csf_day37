package sg.edu.nus.iss.day37_springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AppConfig {
    
    @Value("${DO_STORAGE_KEY}")
    public String accessKey;

    @Value("${DO_STORAGE_SECRETKEY}")
    public String secretKey;

    @Value("${DO_STORAGE_ENDPOINT}")
    public String endpoint;

    @Value("${DO_STORAGE_ENDPOINT_REGION}")
    public String endpointRegion;

    @Bean
    public AmazonS3 createS3Client(){
        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        EndpointConfiguration ep = new EndpointConfiguration(endpoint, endpointRegion);

        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(ep)
            .withCredentials(new AWSStaticCredentialsProvider(cred))
            .build();
    }
}
