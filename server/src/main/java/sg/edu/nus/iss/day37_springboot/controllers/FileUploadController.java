package sg.edu.nus.iss.day37_springboot.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.day37_springboot.services.S3Service;

@Controller
public class FileUploadController {
    
    @Autowired
    private S3Service s3Svc;

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

        }catch(IOException ex){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

        }
        
        JsonObject payload = Json.createObjectBuilder().add("imageKey",key).build();
        return ResponseEntity.ok(payload.toString());

    }

}
