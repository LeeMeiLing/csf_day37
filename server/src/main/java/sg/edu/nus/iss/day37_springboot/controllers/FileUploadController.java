package sg.edu.nus.iss.day37_springboot.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.day37_springboot.models.Post;
import sg.edu.nus.iss.day37_springboot.models.S3Post;
import sg.edu.nus.iss.day37_springboot.services.FileUploadService;
import sg.edu.nus.iss.day37_springboot.services.S3Service;

@Controller
public class FileUploadController {
    
    @Autowired
    private S3Service s3Svc;

    @Autowired
    private FileUploadService ffSvc;

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    @PostMapping(path="/upload", 
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(@RequestPart MultipartFile file, 
                                         @RequestPart String title,
                                         @RequestPart String complain)
    {
        String key = "";
        try{

            key = s3Svc.upload(file);
            ffSvc.upload(file, title, complain);

        }catch(IOException | SQLException ex){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

        }
        
        JsonObject payload = Json.createObjectBuilder().add("imageKey",key).build();
        return ResponseEntity.ok(payload.toString());

    }

    @GetMapping(path="/get-image/{postId}/{imageKey}")
    public ResponseEntity<String>retrieveImage(@PathVariable Integer postId, @PathVariable String imageKey) throws IOException {

        // get image from MySQL
        Optional<Post> r = ffSvc.getPostById(postId);
        Post p = r.get(); // r is empty if Id not found, error
        System.out.println(">> In controller, Post is " + p);

        // get image from S3
        Optional<S3Post> s3Optional = s3Svc.download(imageKey);
        S3Post s = s3Optional.get();
        System.out.println(">> in controller, s3Post: " + s);

        // image is Java type byte[]
        // use base64 encoder to encode byte[] before sending back to angular
        String encodedString = Base64.getEncoder().encodeToString(p.getImage());
        String encodedS3 = Base64.getEncoder().encodeToString(s.getImageContent());

        // need to add base64 prefix before the encoded string
        JsonObject payload = Json.createObjectBuilder()
            .add("image", BASE64_PREFIX + encodedString)
            .add("imageFromS3", BASE64_PREFIX + encodedS3)
            .build();

        return ResponseEntity.ok(payload.toString());
    }

}
