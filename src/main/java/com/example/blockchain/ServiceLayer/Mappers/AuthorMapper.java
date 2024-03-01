package com.example.blockchain.ServiceLayer.Mappers;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.AuthorEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.AuthorDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorMapper {

    public static AuthorEntity mapDTOToEntity(AuthorDTO authorDTO, AuthorEntity authorEntity, ArticleEmbeddable articleEmbeddable) {
        authorEntity.setTitle(authorDTO.getTitle());
        authorEntity.setAuthor_name(authorDTO.getAuthor_name());
        authorEntity.setAuthor_email(authorDTO.getAuthor_email());
        authorEntity.setInstitution(authorDTO.getInstitution());
        authorEntity.setDepartment(authorDTO.getDepartment());
        authorEntity.setAddress(authorDTO.getAddress());
        return authorEntity;
    }

    public static LinkedList<AuthorEntity> mapDTOToEntityList(LinkedList<AuthorDTO> authorDTOs, LinkedList<AuthorEntity> authorEntities, ArticleEmbeddable articleEmbeddable) {
        for(AuthorDTO element: authorDTOs){
            AuthorEntity authorEntity = mapDTOToEntity(element, new AuthorEntity(), articleEmbeddable);
            authorEntities.add(authorEntity);
        }
        return authorEntities;
    }
}
