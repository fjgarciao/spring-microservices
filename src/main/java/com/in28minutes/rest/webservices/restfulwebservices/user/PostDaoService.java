package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostDaoService {

    private static Map<Integer, List<Post>> postsByUser = new HashMap<>();
    private static int postCounter = 3;

    static {
        List<Post> posts1 = new ArrayList<>();
        posts1.add(new Post(1, "Title 1"));
        posts1.add(new Post(2, "Title 2"));

        postsByUser.put(1, posts1);

        List<Post> posts2 = new ArrayList<>();
        posts2.add(new Post(3, "Title 3"));

        postsByUser.put(2, posts2);

        postsByUser.put(3, new ArrayList<>());
    }

    public List<Post> findAll(int userId) {
        return postsByUser.get(userId);
    }

    public Post save(int userId, Post post) {
        if (post.getId() == null) {
            post.setId(++postCounter);
        }

        List<Post> posts;
        if (postsByUser.containsKey(userId)) {
            posts = postsByUser.get(userId);
        } else {
            posts = new ArrayList<>();
            postsByUser.put(userId, posts);
        }

        posts.add(post);
        return post;
    }

    public Post findOne(int userId, int postId) {
        if (postsByUser.containsKey(userId)) {
            return postsByUser.get(userId).stream()
                       .filter(p -> p.getId() == postId)
                       .findFirst()
                       .orElse(null);
        }
        return null;
    }

    public void deleteAll(int userId) {
        if (postsByUser.containsKey(userId)) {
            postsByUser.remove(userId);
        }
    }
}
