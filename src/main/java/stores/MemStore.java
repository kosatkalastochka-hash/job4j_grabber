package stores;

import model.Post;

import java.util.*;

public class MemStore implements Store {

    private long id = 1;
    private final Map<Long, Post> mem = new HashMap<>();

    @Override
    public void save(Post post) {
        post.setId(id);
        mem.put(id++, post);
    }

    @Override
    public List<Post> getAll() {
        return new ArrayList<>(mem.values());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(mem.get(id));
    }
}

