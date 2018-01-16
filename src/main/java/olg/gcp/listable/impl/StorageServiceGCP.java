package olg.gcp.listable.impl;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import olg.gcp.listable.util.StorageService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class StorageServiceGCP implements StorageService {
    private static Storage storage = null;

    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    @Value("${storage.bucket-name}")
    private String bucketName;

    @Override
    public String testMe() {
        return "I am GCP-based, using " + this.bucketName;
    }

    @Override
    public String saveFile(MultipartFile filePart) throws IOException {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        final String fileName = filePart.getOriginalFilename().replaceAll("\\.(?!.*\\.)","-" + dt +".");

        // the inputstream is closed by default, so we don't need to close it here
        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder(bucketName, fileName)
                                // Modify access list to allow all users with link to read file
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .build(),
                        filePart.getInputStream());

        // Create a fixed dedicated URL that points to the GCS hosted file
        ServingUrlOptions options = ServingUrlOptions.Builder
                .withGoogleStorageFileName("/gs/" + bucketName + "/" + fileName)
                .imageSize(150)
                .crop(true)
                .secureUrl(true);
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        String url = imagesService.getServingUrl(options);
        return url;
    }
}
