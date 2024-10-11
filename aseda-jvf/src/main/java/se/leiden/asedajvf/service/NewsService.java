package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.NewsDtoForm;
import se.leiden.asedajvf.dto.NewsDtoView;

import java.util.List;

public interface NewsService {
    NewsDtoView createNews(NewsDtoForm newsDtoForm);
    NewsDtoView updateNews(int id, NewsDtoForm newsDtoForm);
    void deleteNews(int id);
    NewsDtoView getNewsById(int id);
    List<NewsDtoView> getAllNews();
}
