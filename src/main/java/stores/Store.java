package stores;

import model.Post;

import java.util.List;
import java.util.Optional;

public interface Store  {

    void save(Post post);

    public List<Post> getAll();

    public Optional<Post> findById(Long id);
}