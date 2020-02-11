package me.jordy.rest.controller;

import me.jordy.rest.entity.Event;
import me.jordy.rest.entity.EventDto;
import me.jordy.rest.repository.EventRepository;
import me.jordy.rest.resource.ErrorsResource;
import me.jordy.rest.resource.EventEntityModel;
import me.jordy.rest.resource.EventResource;
import me.jordy.rest.validator.EventValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidators;

    // 생성자로 생성시 빈으로 이미 등록되어 있으면 @Autowired를 붙일 필요가 없음.(Spring 4.4 이상 부터)
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidators){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidators = eventValidators;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {

        System.out.println(errors.hasErrors());
        if(errors.hasErrors()) {
//            return ResponseEntity.badRequest().build();
            return badRequest(errors);
        }

        eventValidators.validate(eventDto,errors);

        if(errors.hasErrors()) {
//            return ResponseEntity.badRequest().build();
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        System.out.println("event = "+ event);
        Event newEvent = eventRepository.save(event);
        System.out.println("newEvent = "+ newEvent);
//        URI createdUri = linkTo(methodOn(EventController.class).createEvent(e)).slash("{id}").toUri();
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        // ObjectMapper 가 Object를 JSON으로 변경할 시 BeanSerializer를 사용.
        // 자바 빈 스펙을 준수한 객체를 제이슨으로 변환 가능.
        EventEntityModel eventResource = new EventEntityModel(event);

        eventResource.add(linkTo(EventController.class).withRel("query-events")); //select
        eventResource.add(selfLinkBuilder.withRel("update-event")); //self //update, method 할당은 불가
        eventResource.add(new Link("/docs/index.html#resource-events-created").withRel("profile")); //profile

    return ResponseEntity.created(createdUri).body(eventResource)/*.build()*/;
    }

    // Ctrl + Alt + m -> refactor
    private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
