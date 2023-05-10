package sg.edu.nus.iss.day37_springboot.models;

import java.util.Arrays;

import com.amazonaws.services.s3.model.ObjectMetadata;

public class S3Post{
    
    private ObjectMetadata metaData;
    private byte[] imageContent;

    public ObjectMetadata getMetaData() {
        return metaData;
    }
    public void setMetaData(ObjectMetadata metaData) {
        this.metaData = metaData;
    }
    public byte[] getImageContent() {
        return imageContent;
    }
    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }
    @Override
    public String toString() {
        return "S3Post [metaData=" + metaData + ", imageContent=" + Arrays.toString(imageContent) + "]";
    }

    
    

}
