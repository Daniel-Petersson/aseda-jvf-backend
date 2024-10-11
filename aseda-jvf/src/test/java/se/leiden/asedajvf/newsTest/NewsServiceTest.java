package se.leiden.asedajvf.newsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.leiden.asedajvf.dto.NewsDtoForm;
import se.leiden.asedajvf.dto.NewsDtoView;
import se.leiden.asedajvf.mapper.NewsMapper;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.model.News;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.repository.NewsRepository;
import se.leiden.asedajvf.service.NewsServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNews_ShouldReturnNewsDtoView_WhenNewsIsCreated() {
        // Arrange
        NewsDtoForm newsDtoForm = new NewsDtoForm("Title", "Content", 1, "picUrl", "fileUrl");
        Member author = new Member();
        News news = new News();
        NewsDtoView newsDtoView = new NewsDtoView();

        when(memberRepository.findById(newsDtoForm.getAuthorId())).thenReturn(Optional.of(author));
        when(newsMapper.toNews(newsDtoForm)).thenReturn(news);
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(newsMapper.toDto(news)).thenReturn(newsDtoView);

        // Act
        NewsDtoView result = newsService.createNews(newsDtoForm);

        // Assert
        assertNotNull(result);
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void updateNews_ShouldReturnUpdatedNewsDtoView_WhenNewsIsUpdated() {
        // Arrange
        int newsId = 1;
        NewsDtoForm newsDtoForm = new NewsDtoForm("Updated Title", "Updated Content", 1, "newPicUrl", "newFileUrl");
        News existingNews = new News();
        NewsDtoView updatedNewsDtoView = new NewsDtoView();

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(existingNews)).thenReturn(existingNews);
        when(newsMapper.toDto(existingNews)).thenReturn(updatedNewsDtoView);

        // Act
        NewsDtoView result = newsService.updateNews(newsId, newsDtoForm);

        // Assert
        assertNotNull(result);
        verify(newsRepository, times(1)).save(existingNews);
    }

    @Test
    void deleteNews_ShouldDeleteNews_WhenNewsExists() {
        // Arrange
        int newsId = 1;
        when(newsRepository.existsById(newsId)).thenReturn(true);

        // Act
        newsService.deleteNews(newsId);

        // Assert
        verify(newsRepository, times(1)).deleteById(newsId);
    }

    @Test
    void getNewsById_ShouldReturnNewsDtoView_WhenNewsExists() {
        // Arrange
        int newsId = 1;
        News news = new News();
        NewsDtoView newsDtoView = new NewsDtoView();

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));
        when(newsMapper.toDto(news)).thenReturn(newsDtoView);

        // Act
        NewsDtoView result = newsService.getNewsById(newsId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllNews_ShouldReturnListOfNewsDtoView() {
        // Arrange
        List<News> newsList = List.of(new News(), new News());
        List<NewsDtoView> newsDtoViewList = List.of(new NewsDtoView(), new NewsDtoView());

        when(newsRepository.findAll()).thenReturn(newsList);
        when(newsMapper.toDto(any(News.class))).thenReturn(new NewsDtoView());

        // Act
        List<NewsDtoView> result = newsService.getAllNews();

        // Assert
        assertEquals(newsDtoViewList.size(), result.size());
    }
}
