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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJPAResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public UserJPAResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
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
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        User user = getUserOrException(userId);
        userRepository.delete(user);
    }

    private User getUserOrException(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("userId-%s", userId)));
    }

    @GetMapping("/users/{userId}/posts")
    public List<Post> retrieveAllUserPosts(@PathVariable Integer userId) {
        User user = getUserOrException(userId);
        return user.getPosts();
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public Post retrieveUserPost(@PathVariable Integer userId, @PathVariable Integer postId) {
        User user = getUserOrException(userId);
        return postRepository.findByUserAndId(user, postId).orElseThrow(() -> new NotFoundException(String.format("userId-%s,postId-%s", userId, postId)));
    }

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<?> createPost(@PathVariable Integer userId, @Valid @RequestBody Post post) {
        User user = getUserOrException(userId);
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{userId}/posts/{postId}")
    public void deleteUser(@PathVariable Integer userId, @PathVariable Integer postId) {
        User user = getUserOrException(userId);
        Post post = postRepository.findByUserAndId(user, postId).orElseThrow(() -> new NotFoundException(String.format("userId-%s,postId-%s", userId, postId)));
        postRepository.delete(post);
    }
}
