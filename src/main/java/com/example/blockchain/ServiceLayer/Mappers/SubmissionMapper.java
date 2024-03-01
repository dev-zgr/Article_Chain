package com.example.blockchain.ServiceLayer.Mappers;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.AuthorEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ArticleEmbeddableDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class SubmissionMapper {

    public static ArticleEmbeddable mapDTOToArticleEmbeddable(ArticleEmbeddableDTO articleEmbeddableDTO, ArticleEmbeddable articleEmbeddable) throws IOException {
        articleEmbeddable.setArticle_title(articleEmbeddableDTO.getArticle_title());
        articleEmbeddable.setArticle_type(articleEmbeddableDTO.getArticle_type());
        articleEmbeddable.setArticle_resField(articleEmbeddableDTO.getArticle_resField());
        articleEmbeddable.setArticle_date(articleEmbeddableDTO.getArticle_date());
        articleEmbeddable.setArticle_keywords(articleEmbeddableDTO.getArticle_keywords());
        articleEmbeddable.setPaperAbstract(articleEmbeddableDTO.getPaperAbstract());
        //TODO fix this
        articleEmbeddable.setAuthors(AuthorMapper.mapDTOToEntityList(articleEmbeddableDTO.getAuthors(), new LinkedList<AuthorEntity>(), articleEmbeddable));
        articleEmbeddable.setFileIdentifier(articleEmbeddableDTO.getFileIdentifier());
        return articleEmbeddable;
    }

    public static byte[] convertMultipartFileToByteArray(MultipartFile multipartFile) throws IOException {
        return multipartFile.getBytes();
    }
}





