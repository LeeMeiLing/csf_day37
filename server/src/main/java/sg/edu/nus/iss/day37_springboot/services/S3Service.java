package sg.edu.nus.iss.day37_springboot.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    
    @Autowired
    private AmazonS3 s3Client;

    @Value("${DO_STORAGE_BUCKETNAME}")
    public String bucketName;

    public String upload(MultipartFile file) throws IOException{

        // create a map to store user-defined metadata
        Map<String,String> userData = new HashMap<>();
        userData.put("name", "MeiLing");
        userData.put("uploadDateTime", LocalDateTime.now().toString());
        userData.put("originalFilename", file.getOriginalFilename());

        System.out.println("filename: " + file.getOriginalFilename()); // debug
        System.out.println("content-type: " + file.getContentType()); // debug
        System.out.println("content-length: " + file.getSize()); // debug

        // construct metadata to be uploaded to S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType()); // mandatory
        metadata.setContentLength(file.getSize()); // mandatory
        metadata.setUserMetadata(userData);

        String key = UUID.randomUUID().toString().substring(0, 8);
        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");
        int count = 0;
        String filenameExt = "";
        while(tk.hasMoreTokens()){
            if(count == 1){
                filenameExt = tk.nextToken();
                System.out.println(">>> in if count == 1"); // debug
                System.out.println(filenameExt); // debug
                break;
            }else{
                filenameExt = tk.nextToken();
                count++;
                System.out.println(">>> in else"); // debug
                System.out.println(filenameExt); // debug
            }
        }

        // enforce blob file to png (img file from angular will be blob; blob file cant load on digital ocean, need to download)
        if(filenameExt.equals("blob"))
            filenameExt = filenameExt + ".png";

        // new PutObjectRequest (String bucketName, String key, InputStream input, ObjectMetadata metadata)
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, "myobject%s.%s".formatted(key,filenameExt), 
                                                            file.getInputStream(), metadata);
        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putRequest);

        return "myobject%s.%s".formatted(key,filenameExt);
    }
}
