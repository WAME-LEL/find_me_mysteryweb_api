package com.findme.mysteryweb.api;


import com.findme.mysteryweb.domain.Book;
import com.findme.mysteryweb.domain.Review;
import com.findme.mysteryweb.service.BookService;
import com.findme.mysteryweb.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://detectivesnight.com", "https://www.detectivesnight.com", "http://detectivesnight.com", "http://www.detectivesnight.com"})
public class BookApiController {

    private final BookService bookService;
    private final WebClient webClient;


    @PostMapping("/api/book")
    public ResponseEntity<?> saveBook(@RequestBody SaveBookRequest request){
        Book book = Book.createBook(request.title, request.author, request. publisher, request.plot, request.thumbnail);
        bookService.save(book);

        return ResponseEntity.ok("save");
    }

    @GetMapping("/api/book/info")
    public ResponseEntity<?> bookList(@ModelAttribute BookListRequest request){
        String url = "/v3/search/book";
        String clientId = "c917b58c0e9f8ee5c1240f7280a4d611"; // 클라이언트 ID

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("query", request.query) // 예시 파라미터
                        .queryParam("size", 50)
                        .build())
                .header("Authorization", "KakaoAK "+clientId)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/book")
    @Transactional
    public ResponseEntity<?> savedBookList(@ModelAttribute SavedBookListRequest request){
        List<Book> bookList;

        String searchBy = request.searchBy;
        String searchTerm = request.searchTerm;

        if(request.count != null){
            bookList = bookService.findCountByRecommendCount(request.count);
        }else{
            if(Objects.equals(searchBy, "title")){
                bookList = bookService.findAllByTitle(searchTerm);
            } else if (Objects.equals(searchBy, "author")) {
                bookList = bookService.findAllByAuthor(searchTerm);
            }else{
                bookList = bookService.findAll();
            }
        }
        
        List<SavedBookListResponse> collect = bookList.stream()
                .map(b -> new SavedBookListResponse(b.getId(), b.getTitle(), b.getAuthor(), b.getPublisher(), b.getPlot(), b.getThumbnail(), b.getRecommendationCount(), b.getReviewList().size()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Result<>(collect));
    }

    @GetMapping("/api/book/detail")
    @Transactional
    public ResponseEntity<?> bookInfo(@ModelAttribute BookInfoRequest request){
        Book book = bookService.findOne(request.bookId);
        List<Review> reviewList = book.getReviewList();
        List<ReviewDto> collect = reviewList.stream()
                .map(r -> new ReviewDto(r.getId(), r.getMember().getId(), r.getMember().getNickname(), r.getContent(), r.getDatetime()))
                .collect(Collectors.toList());

        BookInfoResponse bookInfoResponse = new BookInfoResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getPublisher(), book.getPlot(), book.getThumbnail(), collect ,book.getRecommendationCount());

        return ResponseEntity.ok(new Result<>(bookInfoResponse));
    }

    //==DTO==//

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    static class SaveBookRequest{
        private String title;
        private String author;
        private String publisher;
        private String plot;
        private String thumbnail;
    }

    @Data
    static class BookListRequest{
        private String query;
    }

    @Data
    static class SavedBookListRequest{
        private Integer count;
        private String searchTerm;
        private String searchBy;
    }
    @Data
    @AllArgsConstructor
    static class SavedBookListResponse{
        private Long bookId;
        private String title;
        private String author;
        private String publisher;
        private String plot;
        private String thumbnail;
        private int recommendationCount;
        private int reviewCount;
    }

    @Data
    static class BookInfoRequest{
        private Long bookId;
    }

    @Data
    @AllArgsConstructor
    static class BookInfoResponse{
        private Long id;
        private String title;
        private String author;
        private String publisher;
        private String plot;
        private String thumbnail;
        private List<ReviewDto> reviewList;
        private int recommendationCount;
    }


    @Data
    @AllArgsConstructor
    static class ReviewDto{
        private Long reviewId;
        private Long memberId;
        private String nickname;
        private String content;
        private LocalDateTime datetime;
    }
}
