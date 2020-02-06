package me.jordy.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime beginEnrollmentDateTime;

    @NotNull
    private LocalDateTime closeEnrollmentDateTime;

    @NotNull
    private LocalDateTime beginEventDateTime;

    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임

    @Min(0)
    private int basePrice;   // (optional)

    @Min(0)
    private int maxPrice; // (optional)

    @Min(0)
    private int limitOfEnrollment;

    @AssertTrue(message="등록 시작 시간이 등록 종료 시간 보다 더 큽니다.")
    public boolean beginEnrollmentIsLaterThanCloseEnrollment () {
        System.out.println("#########   beginEnrollmentIsLaterThanCloseEnrollment  ######");
        if(beginEnrollmentDateTime.compareTo(closeEnrollmentDateTime) <= 0)
            return false;
        else
            return true;
    }
    @AssertTrue(message="이벤트 시작 시간이 이벤트 종료 시간 보다 더 큽니다.")
    public boolean beginEventIsLaterThanEndEvent () {
        System.out.println("#########   beginEventIsLaterThanEndEvent  ######");
        if(beginEventDateTime.compareTo(endEventDateTime) <= 0)
            return false;
        else
            return true;
    }

}
