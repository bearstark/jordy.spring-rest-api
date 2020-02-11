package me.jordy.rest.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.jordy.rest.entity.Event;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.servlet.resource.ResourceTransformerSupport;

public class EventResource extends RepresentationModel {

    //Array는 unwrapped 되지 않음.
    @JsonUnwrapped
    private Event event;
    // JsonUnwrapped는 아래와 같이 데이터가 나올때 event 밖으로 나옴.
    //  "event":{
    //          "id":1,
    //                  "name":"Spring",
    //                  "description":"Rest API Development Spring",
    //                  "beginEnrollmentDateTime":"2020-01-01T13:00:00",
    //  ...

    public EventResource (Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
