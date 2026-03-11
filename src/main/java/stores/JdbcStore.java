package stores;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private final Connection connection;
    private static final Logger LOG = LoggerFactory.getLogger(JdbcStore.class);

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    private Post createPost(ResultSet rs) throws SQLException {
        Timestamp t = rs.getTimestamp("created");
        Long time = null;
        if (t != null) {
            time = t.getTime();
        }
        return new Post(rs.getLong("id"), rs.getString("name"), rs.getString("text"),
                rs.getString("link"), time);
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO post( name, link, text, created) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT (link) DO NOTHING RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            Long time = post.getTime();
            if (time != null) {
                statement.setTimestamp(4, new Timestamp(time));
            } else {
                statement.setNull(4, Types.TIMESTAMP);
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                post.setId(resultSet.getLong("id"));
            }
        } catch (Exception e) {
            LOG.error("Ошибка при сохранении  поста( save())", e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM post");
            while (rs.next()) {
                Post post = createPost(rs);
                result.add(post);
            }
        } catch (SQLException e) {
            LOG.error("Ошибка при получении списка всех постов из базы данных(getAll())", e);
        }
        return result;
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            return Optional.ofNullable(createPost(rs));
        } catch (SQLException e) {
            LOG.error("Ошибка при поиске поста по  id (findById())  id: " + id, e);
            return Optional.empty();
        }
    }
}