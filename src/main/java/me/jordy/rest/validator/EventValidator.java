package me.jordy.rest.validator;

import me.jordy.rest.entity.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()
            && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue", "baseprice가 이상해!");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice가 이상해!");
        }

        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        LocalDateTime beginEnrollmentDateTime = eventDto.getBeginEnrollmentDateTime();
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if( endEventDateTime.isBefore(beginEventDateTime) ||
                endEventDateTime.isBefore(beginEnrollmentDateTime) ||
                endEventDateTime.isBefore(closeEnrollmentDateTime)) {
            errors.rejectValue("endEventDateTime","wrongValue","종류 시간이 이상해요!");
        }
    }
}
