package com.greyson.projectboard.dto.response;

import com.greyson.projectboard.dto.ArticleWithCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickName,
        Set<ArticleCommentResponse> articleCommentResponses
) implements Serializable {
    public static ArticleWithCommentsResponse of(Long id, String title, String content, String hashtag,
                                                 LocalDateTime createdAt, String email, String nickName,
                                                 Set<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentsResponse(id, title, content, hashtag, createdAt, email, nickName, articleCommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentDto dto){
        String nickName = dto.userAccountDto().nickName();
        if (nickName == null || nickName.isBlank()) {
            nickName = dto.userAccountDto().userId();
        }
        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickName,
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}
