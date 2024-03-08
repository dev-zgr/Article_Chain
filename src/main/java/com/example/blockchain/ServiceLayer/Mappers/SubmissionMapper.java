package com.example.blockchain.ServiceLayer.Mappers;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.AuthorEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ArticleEmbeddableDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.AuthorDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public static ArticleEmbeddableDTO mapArticleEmbeddableToDTO(ArticleEmbeddable articleEmbeddable) {
        ArticleEmbeddableDTO articleEmbeddableDTO = new ArticleEmbeddableDTO();

        articleEmbeddableDTO.setArticle_title(articleEmbeddable.getArticle_title());
        articleEmbeddableDTO.setArticle_type(articleEmbeddable.getArticle_type());
        articleEmbeddableDTO.setArticle_resField(articleEmbeddable.getArticle_resField());
        articleEmbeddableDTO.setArticle_date(articleEmbeddable.getArticle_date());
        articleEmbeddableDTO.setArticle_keywords(articleEmbeddable.getArticle_keywords());
        articleEmbeddableDTO.setPaperAbstract(articleEmbeddable.getPaperAbstract());
        List<AuthorEntity> authors = articleEmbeddable.getAuthors();
        LinkedList<AuthorDTO> authorDTOS = AuthorMapper.mapEntityListToDTOList(new LinkedList<AuthorDTO>(),authors);
        articleEmbeddableDTO.setAuthors(authorDTOS);
        articleEmbeddableDTO.setFileIdentifier(articleEmbeddable.getFileIdentifier());

        return articleEmbeddableDTO;
    }

    public static byte[] convertMultipartFileToByteArray(MultipartFile multipartFile) throws IOException {
        return multipartFile.getBytes();
    }
}





