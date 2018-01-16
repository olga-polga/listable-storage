package olg.gcp.listable.endpoint;

import olg.gcp.listable.util.StorageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
public class HomeController {

    @Value("${storage.bucket-name}")
    private String bucketName;

    @Value("${services.repo}")
    private String listableRepoUrl;
    private static final String msgTemplate = "File '%s' (%d) uploaded to %s for listing id %s";
    private static final String linkResourceUrlTemplate = "%s/houses/%s/pictures";
    private static final String postResourceUrlTemplate = "%s/pictures";

    @Autowired
    public StorageService storageService;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("listingId") String listingId) {

        System.out.println(storageService.testMe());
        String storageUrl = null;
        try {
            storageUrl = storageService.saveFile(file);
            System.out.println(storageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

        RestTemplate restTemplate = new RestTemplate();

        // Add new image url to the data repository:
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        json.put("url", storageUrl);
        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        String postPicUrl = String.format(postResourceUrlTemplate, listableRepoUrl);
        URI location = restTemplate.postForLocation(postPicUrl, httpEntity);
        System.out.println("Added resource: " + location);

        // Link the new image url to the selected listing ID:
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "text/uri-list");
        httpEntity = new HttpEntity<>(location.toString(), httpHeaders);
        String linkUrl = String.format(linkResourceUrlTemplate, listableRepoUrl, listingId);
        ResponseEntity<String> entity = restTemplate.postForEntity(linkUrl, httpEntity, String.class);
        System.out.println(entity.getStatusCode());

        return String.format(msgTemplate, file.getOriginalFilename(), file.getSize(), bucketName, listingId);
    }


    @GetMapping("/")
    public String test()  throws IOException {
                return storageService.testMe();
    }

}
