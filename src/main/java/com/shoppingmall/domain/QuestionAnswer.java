package com.shoppingmall.domain;

import com.shoppingmall.dto.QuestionAnswerResponseDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @CreatedDate
    private LocalDateTime createdDate;

    // 객체들 간의 관계
    @ManyToOne
    @JoinColumn(name = "normal_user_id", referencedColumnName = "id")
    private NormalUser normalUser;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    public QuestionAnswerResponseDto toDto() {

        return QuestionAnswerResponseDto.builder()
                .id(id)
                .normalUser(normalUser)
                .question(question)
                .message(message)
                .createdDate(createdDate.getYear() + "-" + createdDate.getMonthValue() + "-"
                        + createdDate.getDayOfMonth() + " " + createdDate.getHour() + ":" + createdDate.getMinute() + ":"
                        + createdDate.getSecond())
                .build();
    }
}
