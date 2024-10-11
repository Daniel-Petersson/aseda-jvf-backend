package se.leiden.asedajvf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.leiden.asedajvf.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {


}
