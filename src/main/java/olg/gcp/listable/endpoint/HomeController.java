package olg.gcp.listable.endpoint;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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


//    @Value("gs://listable-bucket/my-file.txt")
//    private Resource gcsFile;
//    @GetMapping("/")
//    public String readGcsFile()  throws IOException {
//                return StreamUtils.copyToString(
//                gcsFile.getInputStream(),
//                Charset.defaultCharset()) + "\n";
//    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("listingId") String listingId) {
        RestTemplate restTemplate = new RestTemplate();

        // Add new image url to the data repository:
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        //@TODO:  storageService.store(file);
        json.put("url", file.getOriginalFilename());
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


//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    String writeGcs(@RequestBody String data) throws IOException {
//        try (OutputStream os = ((WritableResource) gcsFile).getOutputStream()) {
//            os.write(data.getBytes());
//        }
//        return "file was updated\n";
//    }

}
