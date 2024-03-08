package com.example.blockchain.PresentationLayer.DataTransferObjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ReviewPendingArticleExtendedDTO extends SubmissionRequestDTO{
    public long referringReviewTXID;

    public ReviewPendingArticleExtendedDTO(ArticleEmbeddableDTO articleEmbeddable, String paperHash, long referringReviewTXID) {
        super(articleEmbeddable, paperHash);
        this.referringReviewTXID = referringReviewTXID;
    }

}
