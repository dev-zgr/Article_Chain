package com.example.blockchain.ServiceLayer.Services.Interfaces.V2;

import com.example.blockchain.DataLayer.Entities.SubmitEntity;

import java.util.List;

public interface ArticleServiceV2 {
    List<SubmitEntity> getAllVerifiedSubmissions(int pageNo, boolean ascending);

    long getPageCount();
}
