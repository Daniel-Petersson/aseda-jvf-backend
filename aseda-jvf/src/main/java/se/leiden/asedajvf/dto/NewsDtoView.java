package se.leiden.asedajvf.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsDtoView {
    private int id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String pictureUrl;
    private String fileUrl;
}
