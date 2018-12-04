package com.in28minutes.rest.webservices.restfulwebservices.user;

public class Post {

    private Integer id;
    private String title;
    private String text;

    protected Post() {
    }

    public Post(Integer id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Post post = (Post)o;

        return id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Post{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", text='" + text + '\'' +
               '}';
    }
}
