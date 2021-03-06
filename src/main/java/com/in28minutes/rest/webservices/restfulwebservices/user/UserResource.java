package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.in28minutes.rest.webservices.restfulwebservices.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
public class UserResource {

    private UserDaoService userDaoService;
    private PostDaoService postDaoService;

    @Autowired
    public UserResource(UserDaoService userDaoService, PostDaoService postDaoService) {
        this.userDaoService = userDaoService;
        this.postDaoService = postDaoService;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{userId}")
    public Resource<User> retrieveUser(@PathVariable Integer userId) {
        User user = getUserOrException(userId);

        // HATEOAS
        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        User savedUser = userDaoService.save(user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        if (!userDaoService.deleteById(userId)) {
            throw new NotFoundException(String.format("userId-%s", userId));
        } else {
            postDaoService.deleteAll(userId);
        }
    }

    @GetMapping("/users/{userId}/posts")
    public List<Post> retrieveAllUserPosts(@PathVariable Integer userId) {
        getUserOrException(userId);
        return postDaoService.findAll(userId);
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public Post retrieveUserPost(@PathVariable Integer userId, @PathVariable Integer postId) {
        getUserOrException(userId);
        return getPostOrException(userId, postId);
    }

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<?> createPost(@PathVariable Integer userId, @RequestBody Post post) {
        getUserOrException(userId);

        Post savedPost = postDaoService.save(userId, post);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    private User getUserOrException(Integer userId) {
        User user = userDaoService.findOne(userId);
        if (user == null)
            throw new NotFoundException(String.format("userId-%s", userId));
        return user;
    }

    private Post getPostOrException(Integer userId,Integer postId) {
        Post post = postDaoService.findOne(userId, postId);
        if (post == null)
            throw new NotFoundException(String.format("userId-%s,postId-%s", userId, postId));
        return post;
    }
}
