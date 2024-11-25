package com.greyson.projectboard.dto.response;

import com.greyson.projectboard.dto.ArticleDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickName
) implements Serializable {
    public static ArticleResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt,
                                     String email, String nickName) {
        return new ArticleResponse(id, title, content, hashtag, createdAt, email, nickName);
    }

    public static ArticleResponse from(ArticleDto dto){
        String nickName = dto.userAccountDto().nickName();
        if(nickName == null || nickName.isBlank()){
            nickName = dto.userAccountDto().userId();
        }
        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickName
        );
    }
}
