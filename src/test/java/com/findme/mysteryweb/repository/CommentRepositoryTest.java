package com.findme.mysteryweb.Repository;

import com.findme.mysteryweb.domain.Comment;
import com.findme.mysteryweb.domain.Member;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.repository.CommentRepository;
import jakarta.transaction.Transactional;
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
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    public void afterEach(){
        commentRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Comment comment = Comment.createComment(new Post(), null, new Member(), "test");

        //when
        commentRepository.save(comment);

        //then
        Comment findOne = commentRepository.findOne(comment.getId());
        assertThat(comment).isEqualTo(findOne);

    }

    @Test
    void findOne() {
        //given
        Comment comment1 = Comment.createComment(new Post(), null, new Member(),  "test1");
        Comment comment2 = Comment.createComment(new Post(), null, new Member(), "test2");

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //then
        Comment findOne = commentRepository.findOne(comment1.getId());
        assertThat(comment1).isEqualTo(findOne);

    }

    @Test
    void findAll() {
        //given
        Comment comment1 = Comment.createComment(new Post(),null, new Member(),  "test1");
        Comment comment2 = Comment.createComment(new Post(),null, new Member(),  "test2");

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //then
        List<Comment> findAll = commentRepository.findAll();

        assertThat(findAll.size()).isEqualTo(2);
    }

    @Test
    void findAllByPostId() {
        //given
        Post post1 = Post.createPost("post", "post", "test", "test", "test", new Member());
        Post post2 = Post.createPost("post", "post", "test", "test", "test", new Member());

        Comment comment1 = Comment.createComment(post1,null, new Member(),  "test1");
        Comment comment2 = Comment.createComment(post1,null, new Member(),  "test2");
        Comment comment3 = Comment.createComment(post2,null, new Member(),  "test3");

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //then
        List<Comment> allByPostId = commentRepository.findAllByPostId(post1.getId());

        assertThat(allByPostId.size()).isEqualTo(2);

    }

    @Test
    void findAllByMemberId() {
        //given
        Post post1 = Post.createPost("post", "post", "test", "test", "test", new Member());
        Post post2 = Post.createPost("post", "post", "test", "test", "test", new Member());

        Member member = Member.createMember("jack", "jack", "jack");

        Comment comment1 = Comment.createComment(post1,null, member,  "test1");
        Comment comment2 = Comment.createComment(post1,null, member,  "test2");
        Comment comment3 = Comment.createComment(post2,null, new Member(),  "test3");

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //then
        List<Comment> allByMemberId = commentRepository.findAllByMemberId(member.getId());

        assertThat(allByMemberId.size()).isEqualTo(2);

    }

    @Test
    void delete() {
        //given
        Comment comment1 = Comment.createComment(new Post(),null, new Member(),  "test1");

        //when
        commentRepository.save(comment1);
        commentRepository.delete(comment1.getId());

        //then
        assertThat(commentRepository.findOne(comment1.getId())).isNull();

    }


    @Test
    void findAllTopLevelCommentsByPostId() {
        //given
        Post post = Post.createPost("post", "post", "test", "test", "test", new Member());

        Comment comment1 = Comment.createComment(post,null, new Member(),  "test1");
        Comment comment2 = Comment.createComment(post,null, new Member(),  "test1");

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //then
        List<Comment> allTopLevelCommentsByPostId = commentRepository.findAllTopLevelCommentsByPostId(post.getId());

        assertThat(allTopLevelCommentsByPostId.size()).isEqualTo(2);


    }

    @Test
    void findRepliesByCommentId() {
        //given
        Post post = Post.createPost("post", "post", "test", "test", "test", new Member());

        Comment comment1 = Comment.createComment(post,null, new Member(),  "test1");
        Comment comment2 = Comment.createComment(post,comment1, new Member(),  "test1");
        Comment comment3 = Comment.createComment(post,comment1, new Member(),  "test1");

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //then
        List<Comment> repliesByCommentId = commentRepository.findRepliesByCommentId(comment1.getId());

        assertThat(repliesByCommentId.size()).isEqualTo(2);


    }
}