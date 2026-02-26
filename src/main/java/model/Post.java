package model;

import java.util.Objects;

public class Post {
    Long id;
    String title;
    String link;
    String description;
    Long time;

    public Post(Long id, String title, String description, String link, Long time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.time = time;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append(", id=").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append("description='").append(description).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}
