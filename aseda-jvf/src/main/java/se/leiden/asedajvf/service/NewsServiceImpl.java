package se.leiden.asedajvf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.leiden.asedajvf.dto.NewsDtoForm;
import se.leiden.asedajvf.dto.NewsDtoView;
import se.leiden.asedajvf.mapper.NewsMapper;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.News;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.repository.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final MemberRepository memberRepository;
    private final NewsMapper newsMapper;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, MemberRepository memberRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.memberRepository = memberRepository;
        this.newsMapper = newsMapper;
    }

    @Override
    @Transactional
    public NewsDtoView createNews(NewsDtoForm newsDtoForm) {
        Member author = memberRepository.findById(newsDtoForm.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));
        News news = newsMapper.toNews(newsDtoForm);
        news.setAuthor(author);
        news = newsRepository.save(news);
        return newsMapper.toDto(news);
    }

    @Override
    @Transactional
    public NewsDtoView updateNews(int id, NewsDtoForm newsDtoForm) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
        existingNews.setTitle(newsDtoForm.getTitle());
        existingNews.setContent(newsDtoForm.getContent());
        existingNews.setPictureUrl(newsDtoForm.getPictureUrl());
        existingNews.setFileUrl(newsDtoForm.getFileUrl());
        existingNews.setDateModified(LocalDateTime.now());
        newsRepository.save(existingNews);
        return newsMapper.toDto(existingNews);
    }

    @Override
    @Transactional
    public void deleteNews(int id) {
        if (!newsRepository.existsById(id)) {
            throw new IllegalArgumentException("News not found");
        }
        newsRepository.deleteById(id);
    }

    @Override
    public NewsDtoView getNewsById(int id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
        return newsMapper.toDto(news);
    }

    @Override
    public List<NewsDtoView> getAllNews() {
        return newsRepository.findAll().stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }
}
