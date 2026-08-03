package food.delivery.catalog_ms.core.application.ports.out;

import java.io.InputStream;

public interface ObjectStorageOutputPort {

    void put(String key, InputStream body, long contentLength, String contentType);

    String publicUrl(String key);

    void delete(String key);
}
