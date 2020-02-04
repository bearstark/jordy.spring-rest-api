package me.jordy.rest.controller;

import me.jordy.rest.entity.Event;
import me.jordy.rest.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    EventRepository eventRepository;

    // 생성자로 생성시 빈으로 이미 등록되어 있으면 @Autowired를 붙일 필요가 없음.(Spring 4.4 이상 부터)
    public EventController(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {

        Event newEvent = eventRepository.save(event);
//        URI createdUri = linkTo(methodOn(EventController.class).createEvent(e)).slash("{id}").toUri();
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        return ResponseEntity.created(createdUri).body(event)/*.build()*/;
    }
}
