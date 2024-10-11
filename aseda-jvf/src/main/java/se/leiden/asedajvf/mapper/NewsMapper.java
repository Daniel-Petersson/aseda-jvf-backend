package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import se.leiden.asedajvf.dto.NewsDtoForm;
import se.leiden.asedajvf.dto.NewsDtoView;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.News;
import se.leiden.asedajvf.repository.MemberRepository;

import java.time.LocalDateTime;

@Component
public class NewsMapper {

    @Autowired
    private MemberRepository memberRepository;

    public NewsDtoView toDto(News news) {
        if (news == null) {
            return null;
        }
        return NewsDtoView.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .authorName(news.getAuthor().getFirstName() + " " + news.getAuthor().getLastName())
                .dateCreated(news.getDateCreated())
                .dateModified(news.getDateModified())
                .pictureUrl(news.getPictureUrl())
                .fileUrl(news.getFileUrl())
                .build();
    }

    public News toNews(NewsDtoForm newsDtoForm) {
        Member author = memberRepository.findById(newsDtoForm.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));

        return News.builder()
                .title(newsDtoForm.getTitle())
                .content(newsDtoForm.getContent())
                .author(author)
                .dateCreated(LocalDateTime.now())
                .pictureUrl(newsDtoForm.getPictureUrl())
                .fileUrl(newsDtoForm.getFileUrl())
                .build();
    }
}
