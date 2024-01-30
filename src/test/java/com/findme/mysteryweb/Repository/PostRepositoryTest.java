package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @AfterEach
    void afterEach(){
        postRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Post post = Post.createPost("test", "test", "test", "test","test", new Member());

        //when
        postRepository.save(post);

        //then
        Post findOne = postRepository.findOne(post.getId());
        assertThat(findOne).isEqualTo(post);
    }

    @Test
    void findOne() {
        //given
        Post post = Post.createPost("test", "test", "test", "test","test", new Member());

        //when
        postRepository.save(post);

        //then
        Post findOne = postRepository.findOne(post.getId());
        assertThat(findOne).isEqualTo(post);
    }

    @Test
    void findAll() {
        //given
        Post post1 = Post.createPost("test", "test", "test", "test","test", new Member());
        Post post2 = Post.createPost("test", "test", "test", "test","test", new Member());

        //when
        postRepository.save(post1);
        postRepository.save(post2);

        //then
        List<Post> findAll = postRepository.findAll();
        assertThat(findAll.size()).isEqualTo(2);
    }

    @Test
    void findAllByType(){
        //given
        Post post1 = Post.createPost("test1", "test1", "test1", "test","test", new Member());
        Post post2 = Post.createPost("test2", "test2", "test2", "test","test", new Member());

        //when
        postRepository.save(post1);
        postRepository.save(post2);

        //then
        List<Post> allByType = postRepository.findAllByType("test1");
        assertThat(allByType.size()).isEqualTo(1);
    }

    @Test
    void findAllByTitle(){
        //given
        Post post1 = Post.createPost("test1", "test1", "test1", "test","test", new Member());
        Post post2 = Post.createPost("test2", "test2", "test2", "test","test", new Member());

        //when
        postRepository.save(post1);
        postRepository.save(post2);

        //then
        List<Post> test1 = postRepository.findAllByTitle("test1");
        Assertions.assertThat(test1.size()).isEqualTo(1);
    }

    @Test
    void findAllByTypeAndTitle(){
        //given
        Post post1 = Post.createPost("test1", "test1", "test1","test", "test", new Member());
        Post post2 = Post.createPost("test1", "test2", "test2","test", "test", new Member());

        //when
        postRepository.save(post1);
        postRepository.save(post2);

        //then
        List<Post> test1 = postRepository.findAllByTypeAndTitle("test1", "test1");
        Assertions.assertThat(test1.size()).isEqualTo(1);

    }

    @Test
    void delete() {
        //given
        Post post1 = Post.createPost("test", "test", "test", "test","test", new Member());
        Post post2 = Post.createPost("test", "test", "test", "test","test", new Member());

        //when
        postRepository.save(post1);
        postRepository.save(post2);

        postRepository.delete(post1.getId());

        //then
        List<Post> findAll = postRepository.findAll();
        assertThat(findAll.size()).isEqualTo(1);

    }
}