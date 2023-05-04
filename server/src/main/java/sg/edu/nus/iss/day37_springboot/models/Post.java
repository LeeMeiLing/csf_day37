package sg.edu.nus.iss.day37_springboot.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post implements Serializable{
    private String complain;
    private String title;
    private byte[] image; // may need to change image to file
    private Integer postId;

    public String getComplain() {
        return complain;
    }
    public void setComplain(String complain) {
        this.complain = complain;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public static Post populate(ResultSet rs) throws SQLException{
        final Post p = new Post();
        p.setPostId(rs.getInt("id"));
        p.setComplain(rs.getString("complain"));
        p.setTitle(rs.getString("title"));
        p.setImage(rs.getBytes("blobc"));

        return p;

    }
    @Override
    public String toString() {
        return "Post [complain=" + complain + ", title=" + title + ", postId=" + postId + "]";
    }

    
}
