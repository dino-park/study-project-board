package com.greyson.projectboard.dto.request;

import com.greyson.projectboard.dto.ArticleCommentDto;
import com.greyson.projectboard.dto.UserAccountDto;

/**
 * DTO for {@link com.greyson.projectboard.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }
}