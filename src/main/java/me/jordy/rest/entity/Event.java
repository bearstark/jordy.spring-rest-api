package me.jordy.rest.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
// 기선님의 경우 상호 참조를 방지하기 위해 id와 같은 고유 값만 사용.
// 연관 관계 만드는 필드는 절대 X (스택 오버 플로우가 발생할 수 있음)

// @Data 어노태이션은 사용하지 않는데,
// 그 이유는 모든 필드를 가지고 equals와 hashcode를 구현.
public class Event {

    private Integer id;

    private String name; private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice;   // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus = EventStatus.DRAFT;

}