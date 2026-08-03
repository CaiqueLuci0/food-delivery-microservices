package food.delivery.catalog_ms.infra.adapters.outbound.storage;

import food.delivery.catalog_ms.core.application.ports.out.ObjectStorageOutputPort;
import food.delivery.catalog_ms.infra.config.S3Properties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

public class S3ObjectStorageAdapter implements ObjectStorageOutputPort {

    private final S3Client s3Client;
    private final S3Properties properties;

    public S3ObjectStorageAdapter(S3Client s3Client, S3Properties properties) {
        this.s3Client = s3Client;
        this.properties = properties;
    }

    @Override
    public void put(String key, InputStream body, long contentLength, String contentType) {
        PutObjectRequest.Builder request = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(key);
        if (contentType != null && !contentType.isBlank()) {
            request.contentType(contentType);
        }
        s3Client.putObject(request.build(), RequestBody.fromInputStream(body, contentLength));
    }

    @Override
    public String publicUrl(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        String base = properties.getPublicBaseUrl();
        if (base.endsWith("/")) {
            return base + key;
        }
        return base + "/" + key;
    }

    @Override
    public void delete(String key) {
        if (key == null || key.isBlank()) {
            return;
        }
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(key)
                .build());
    }
}
