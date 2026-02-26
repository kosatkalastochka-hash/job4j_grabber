package model;

import java.util.Objects;

public class Post {
    Long id;
    String title;
    String link;
    String description;
    Long time;

    public Post(String description, Long id, String link, Long time, String title) {
        this.description = description;
        this.id = id;
        this.link = link;
        this.time = time;
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Post post = (Post) object;
        return Objects.equals(title, post.title) && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link);
    }
}
