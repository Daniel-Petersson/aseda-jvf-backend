package se.leiden.asedajvf.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column
    private LocalDateTime dateModified;

    private String pictureUrl;
    private String fileUrl;
}
