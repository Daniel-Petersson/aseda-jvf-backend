package se.leiden.asedajvf.newsTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.leiden.asedajvf.model.News;
import se.leiden.asedajvf.repository.NewsRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void saveNews_ShouldPersistNews() {
        // Arrange
        News news = new News();
        news.setTitle("Test Title");
        news.setContent("Test Content");

        // Act
        News savedNews = newsRepository.save(news);

        // Assert
        assertNotNull(savedNews.getId());
    }
}
