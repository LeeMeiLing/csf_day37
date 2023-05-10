package sg.edu.nus.iss.day37_springboot.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.day37_springboot.models.Post;

@Repository
public class FileUploadRepository {

    private static final String INSERT_POST_SQL = "INSERT INTO posts (blobc,title,complain) VALUES (?,?,?)";
    
    private static final String SQL_GET_POST_BY_POSTID = "select id,blobc,title,complain from posts where id = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate JdbcTemplate;

    public void upload(MultipartFile file, String title, String complain) throws SQLException, IOException{
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_POST_SQL))
        {
                InputStream is = file.getInputStream();

                // ps.setBinaryStream(int parameterIndex, InputStream x, long length)
                ps.setBinaryStream(1, is , file.getSize());
                ps.setString(2, title);
                ps.setString(3, complain);
                ps.executeUpdate();

        }
    }

    public Optional<Post> getPostById(Integer postId){

        return JdbcTemplate.query(SQL_GET_POST_BY_POSTID, (ResultSet rs) -> {
                if(!rs.next()){
                    return Optional.empty(); // Tested, ResultSet is null if ID not found
                }

                final Post post = Post.populate(rs);
                /*
                 *  final Post p = new Post();
                    p.setPostId(rs.getInt("id"));
                    p.setComplain(rs.getString("complain"));
                    p.setTitle(rs.getString("title"));
                    p.setImage(rs.getBytes("blobc"));   // use getBytes() to retrieve sql Blob  & store to byte[] in java

                 */
                return Optional.of(post);

            }, postId);
    }


}
