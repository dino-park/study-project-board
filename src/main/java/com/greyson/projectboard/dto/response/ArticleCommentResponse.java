package com.greyson.projectboard.dto.response;

import com.greyson.projectboard.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickName
) implements Serializable {
    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickName) {
        return new ArticleCommentResponse(id, content, createdAt, email, nickName);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto){
        String nickName = dto.userAccountDto().nickName();
        if(nickName == null || nickName.isBlank()){
            nickName = dto.userAccountDto().userId();
        }
        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickName
        );
    }
}
