package me.jordy.rest.resource;

import me.jordy.rest.controller.EventController;
import me.jordy.rest.entity.Event;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventEntityModel extends EntityModel<Event> {
    public EventEntityModel(Event event, Link...links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
