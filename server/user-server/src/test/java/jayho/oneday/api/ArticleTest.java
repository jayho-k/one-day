package jayho.oneday.api;


import jayho.oneday.data.DataInitialize;
import jayho.oneday.service.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ArticleTest {


    @LocalServerPort
    int port;
    RestClient restClient;
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    DataInitialize dataInitialize;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",port));
//        dataInitialize.clearArticle();
//        dataInitialize.initArticle();
    }

    @DisplayName("게시글 생성 성공 테스트")
    @Test
    void createArticleTest() {
        Long writerId = 1L;
        List<String> images = List.of("test-image.png", "image2.png", "image3.png");

        String content = "content";

        ResponseEntity<BaseResponse> res1 = createArticle(ArticleCreateRequest.builder()
                .images(images)
                .content(content)
                .writerId(writerId)
                .build());

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }



    @DisplayName("게시글 생성 시 image null BAD_REQUEST 테스트")
    @Test
    void createArticle_imageNullTest() {
        Long writerId = 1L;
        String content = "content";

        ResponseEntity<BaseResponse> res2 = createArticle4xx(ArticleCreateRequest.builder()
                .images(null)
                .content(content)
                .writerId(writerId)
                .build());

        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @DisplayName("게시글 생성 시 writerId null BAD_REQUEST 테스트")
    @Test
    void createArticle_writerIdNullTest() {
        List<String> images = List.of("image1.png", "image2.png", "image3.png");
        String content = "content";

        ResponseEntity<BaseResponse> res3 = createArticle4xx(ArticleCreateRequest.builder()
                .images(images)
                .content(content)
                .writerId(null)
                .build());

        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    ResponseEntity<BaseResponse> createArticle(ArticleCreateRequest request){
        return restClient.post()
                .uri("/article")
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> createArticle4xx(ArticleCreateRequest request){
        return restClient.post()
                .uri("/article")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Test
    void uploadArticleImageTest() throws IOException {
        ClassPathResource path = new ClassPathResource("test-image.png");
        FileSystemResource articleImage = new FileSystemResource(path.getFile());
        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("articleImage", articleImage);
        uploadArticleImage(request);
    }

    ResponseEntity<BaseResponse> uploadArticleImage(MultiValueMap<String, Object> request) {
        return restClient.post()
                .uri("/article/upload/image")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }


    @Getter
    @Builder
    @AllArgsConstructor
    static class ArticleCreateRequest{
        private List<String> images;
        private String content;
        private Long writerId;
    }

    @DisplayName("게시글 조회 성공 테스트")
    @Test
    void readArticleTest() {
        Long articleId = 1L;

        ResponseEntity<BaseResponse> res1 = readArticle(articleId);
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("게시글 조회 시 articleId null 값 NOT_FOUND 테스트")
    @Test
    void readArticle_articleIdNullTest() {
        ResponseEntity<BaseResponse> res2 = readArticle4xx(null);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> readArticle(Long articleId) {
        return restClient.get()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readArticle4xx(Long articleId) {
        return restClient.get()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @DisplayName("게시글 전체 조회 성공 테스트")
    @Test
    void readAllArticleTest() {
        Integer pageSize = 1;
        Long lastArticleId = 1L;

        ResponseEntity<BaseResponse> res1 = readAllArticle(pageSize, lastArticleId);
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("게시글 전체 조회 lastArticleId null  성공 테스트")
    @Test
    void readAllArticle_lastArticleIdNullTest() {
        Integer pageSize = 1;
        Long lastArticleId = 1L;

        ResponseEntity<BaseResponse> res2 = readAllArticleFirstPage4xx(pageSize, null);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("게시글 전체 조회 pageSize null  BAD_REQUEST 테스트")
    @Test
    void readAllArticle_pageSizeNullTest() {
        Integer pageSize = 1;
        Long lastArticleId = 1L;

        ResponseEntity<BaseResponse> res3 = readAllArticleFirstPage4xx(null, lastArticleId);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<BaseResponse> readAllArticle(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article-all?pageSize={pageSize}&lastArticleId={lastArticleId}", pageSize, lastArticleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readAllArticleFirstPage(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article-all?pageSize={pageSize}", pageSize)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readAllArticleFirstPage4xx(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article-all?pageSize={pageSize}", pageSize)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(BaseResponse.class);
    }

    @DisplayName("게시글 수정 성공 테스트")
    @Test
    void updateArticleTest(){
        ResponseEntity<BaseResponse> res1 = updateArticle(ArticleUpdateRequest.builder()
                .articleId(1L)
                .images(List.of("image1.png"))
                .content("content1")
                .build());

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("게시글 수정 시 articleId null BAD_REQUEST 테스트")
    @Test
    void updateArticle_articleIdNullTest(){

        ResponseEntity<BaseResponse> res2 = updateArticle4xx(ArticleUpdateRequest.builder()
                .articleId(null)
                .images(List.of("image1.png"))
                .content("content123")
                .build());

        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    ResponseEntity<BaseResponse> updateArticle(ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/article")
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> updateArticle4xx(ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/article")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class ArticleUpdateRequest{
        private Long articleId;
        private List<String> images;
        private String content;
    }

    @DisplayName("게시글 임시 삭제 성공 테스트")
    @Test
    void tempDeleteArticleTest() {
        ResponseEntity<BaseResponse> res1 = tempDeleteArticle(1L);
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("게시글 임시 삭제 시 articleId null BAD_REQUEST 테스트")
    @Test
    void tempDeleteArticle_articleIdNullTest() {
        ResponseEntity<BaseResponse> res2 = tempDeleteArticle4xx(null);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> tempDeleteArticle(Long articleId) {
        return restClient.put()
                .uri("/article/temp-delete/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> tempDeleteArticle4xx(Long articleId) {
        return restClient.put()
                .uri("/article/temp-delete/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @DisplayName("게시글 저장 성공 테스트")
    @Test
    void saveArticleTest() {
        Long articleId = 1L;
        Long userId = 1L;

        ResponseEntity<BaseResponse> res1 = saveArticle(articleId, userId);
        ResponseEntity<BaseResponse> res2 = saveArticle4xx(null, userId);
        ResponseEntity<BaseResponse> res3 = saveArticle4xx(articleId, null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("게시글 저장 시 articleId null NOT_FOUND 테스트")
    @Test
    void saveArticle_articleIdNullTest() {
        Long articleId = 1L;
        Long userId = 1L;

        ResponseEntity<BaseResponse> res2 = saveArticle4xx(null, userId);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("게시글 저장 시 userId null NOT_FOUND 테스트")
    @Test
    void saveArticle_userIdNullTest() {
        Long articleId = 1L;
        Long userId = 1L;

        ResponseEntity<BaseResponse> res3 = saveArticle4xx(articleId, null);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> saveArticle(Long articleId, Long userId) {
        return restClient.post()
                .uri("/article/{articleId}/user/{userId}/save",articleId, userId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> saveArticle4xx(Long articleId,Long userId) {
        return restClient.post()
                .uri("/article/{articleId}/user/{userId}/save",articleId, userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }
}
