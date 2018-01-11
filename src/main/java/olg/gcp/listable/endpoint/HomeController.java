package olg.gcp.listable.endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@RestController
public class HomeController {

    @Value("gs://listable-bucket/my-file.txt")
    private Resource gcsFile;
    @GetMapping("/")
    public String readGcsFile()  throws IOException {
                return StreamUtils.copyToString(
                gcsFile.getInputStream(),
                Charset.defaultCharset()) + "\n";
    }




    @RequestMapping(value = "/", method = RequestMethod.POST)
    String writeGcs(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) gcsFile).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated\n";
    }

}
