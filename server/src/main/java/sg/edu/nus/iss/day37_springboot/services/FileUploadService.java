package sg.edu.nus.iss.day37_springboot.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.day37_springboot.models.Post;
import sg.edu.nus.iss.day37_springboot.repositories.FileUploadRepository;

@Service
public class FileUploadService {
    
    @Autowired
    private FileUploadRepository repo;


    public void upload(MultipartFile file, String title, String complain) throws SQLException, IOException{
        repo.upload(file, title, complain);
    }

    public Optional<Post> getPostById(Integer postId){

        Optional<Post> result = repo.getPostById(postId);

        if(result.isPresent()){
            System.out.println(">>> in svc, result present"); // debug
            return result;
        }else{
            System.out.println(">>> in svc, result is empty"); // debug
            return result;
        }

        // return repo.getPostById(postId);
    }
}
