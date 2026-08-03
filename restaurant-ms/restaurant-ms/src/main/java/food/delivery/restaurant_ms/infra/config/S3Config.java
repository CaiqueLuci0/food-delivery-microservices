package food.delivery.restaurant_ms.infra.config;

import food.delivery.restaurant_ms.core.application.ports.out.ObjectStorageOutputPort;
import food.delivery.restaurant_ms.infra.adapters.outbound.storage.S3ObjectStorageAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

    @Bean
    S3Client s3Client(S3Properties properties) {
        return S3Client.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(credentialsProvider())
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

    @Bean
    ObjectStorageOutputPort objectStorageOutputPort(S3Client s3Client, S3Properties properties) {
        return new S3ObjectStorageAdapter(s3Client, properties);
    }

    private static software.amazon.awssdk.auth.credentials.AwsCredentialsProvider credentialsProvider() {
        String accessKey = System.getenv().getOrDefault("AWS_ACCESS_KEY_ID", "test");
        String secretKey = System.getenv().getOrDefault("AWS_SECRET_ACCESS_KEY", "test");
        if ("test".equals(accessKey)) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        }
        return DefaultCredentialsProvider.create();
    }
}
