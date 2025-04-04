package kr.co.greendae.dto.event;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {

    private String title;
    private String date;

}
