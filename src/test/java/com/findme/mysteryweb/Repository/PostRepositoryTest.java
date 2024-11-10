package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.MemberRepository;
import com.findme.mysteryweb.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach(){
        postRepository.clearStore();
        memberRepository.clearStore();
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


    @Test
    void findAllByMemberId() {
        //given
        Member member = Member.createMember("test", "test", "test");

        Post post1 = Post.createPost("test1", "test1", "test1", "test","test", member);
        Post post2 = Post.createPost("test2", "test2", "test2", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post1);
        postRepository.save(post2);

        //then
        List<Post> findAllByMemberId = postRepository.findAllByMemberId(member.getId());

        assertThat(findAllByMemberId.size()).isEqualTo(2);
    }

    @Test
    void findAllByAuthor() {
        //given
        Member member = Member.createMember("test", "test", "test");

        Post post1 = Post.createPost("test1", "test1", "test1", "test","test", member);
        Post post2 = Post.createPost("test1", "test1", "test1", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post1);
        postRepository.save(post2);

        //then
        List<Post> allByAuthor = postRepository.findAllByAuthor(member.getNickname());

        assertThat(allByAuthor.size()).isEqualTo(2);

    }

    @Test
    void findAllByTypeAndTitleOrContent() {
        //given
        Member member = Member.createMember("test", "test", "test");

        Post post = Post.createPost("test1", "test1", "test1", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post);

        //then
        List<Post> allByTypeAndTitleOrContent = postRepository.findAllByTypeAndTitleOrContent("test1", "test1", "asd");

        assertThat(allByTypeAndTitleOrContent.size()).isEqualTo(1);
    }

    @Test
    void findAllByTypeAndAuthor() {
        //given
        Member member = Member.createMember("test", "test", "test");

        Post post1 = Post.createPost("test1", "test1", "ko", "test","test", member);
        Post post2 = Post.createPost("test1", "test1", "koko", "test","test", member);
        Post post3 = Post.createPost("test1", "test1", "koko", "test","test", member);

        //when
        memberRepository.save(member);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        //then
        List<Post> allByTypeAndAuthor = postRepository.findAllByTypeAndAuthor("koko", "test");

        assertThat(allByTypeAndAuthor.size()).isEqualTo(2);
    }

}