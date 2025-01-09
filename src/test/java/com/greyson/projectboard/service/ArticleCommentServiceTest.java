package com.greyson.projectboard.service;

import com.greyson.projectboard.domain.Article;
import com.greyson.projectboard.domain.ArticleComment;
import com.greyson.projectboard.domain.Hashtag;
import com.greyson.projectboard.domain.UserAccount;
import com.greyson.projectboard.dto.ArticleCommentDto;
import com.greyson.projectboard.dto.UserAccountDto;
import com.greyson.projectboard.repository.ArticleCommentRepository;
import com.greyson.projectboard.repository.ArticleRepository;
import com.greyson.projectboard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("게시글ID 조회 시, 해당 댓글 리스트를 반환")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments(){
        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
        // When
        List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);
        // Then
        assertThat(actual).hasSize(1).first().hasFieldOrPropertyWithValue("content", expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    @DisplayName("댓글 내용 입력 시, 댓글을 저장")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComments(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 저장 시도 후, 맞는 게시글이 없으면 경고 로그를 찍고 아무것도 안함.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).shouldHaveNoInteractions();
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 수정내용 입력 후 , 댓글을 수정 및 저장")
    @Test
    void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComments(){
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updatedContent);
        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);
        // When
        sut.updateArticleComment(dto);
        // Then
        assertThat(articleComment.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updatedContent);
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 댓글 정보를 수정 시도 시, 경고 로그를 찍고 아무것도 하지 않음")
    @Test
    void givenNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);
        // When
        sut.updateArticleComment(dto);
        // Then
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID 입력 시, 댓글 삭제")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComments(){
        // Given
        Long articleCommentId = 1L;
        String userId = "dino";
        willDoNothing().given(articleCommentRepository).deleteByIdAndUserAccount_UserId(articleCommentId, userId);
        // When
        sut.deleteArticleComment(articleCommentId, userId);
        // Then
        then(articleCommentRepository).should().deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "dino",
                LocalDateTime.now(),
                "dino"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "dino",
                "password",
                "dino@mail.com",
                "Dino",
                "This is memo",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "dino",
                "dino"
        );
    }

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(
                createArticle(),
                createUserAccount(),
                content
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "dino",
                "password",
                "dino@mail.com",
                "Dino",
                null
        );
    }

    private Article createArticle() {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        article.addHashtags(Set.of(createHashtag(article)));

        return article;
    }

    private Hashtag createHashtag(Article article) {
        return Hashtag.of("java");
    }

}
