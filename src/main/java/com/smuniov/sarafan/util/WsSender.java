package com.smuniov.sarafan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.smuniov.sarafan.dto.EventType;
import com.smuniov.sarafan.dto.ObjectType;
import com.smuniov.sarafan.dto.WsEventDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class WsSender {
    //responsible for sending mess via wessage queue
    private SimpMessagingTemplate template;
    //serialize/deserialize objects
    private final ObjectMapper mapper;

    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    /*
    need to transfer objects via socket
    to send object via socket, we need 4 params:
        * type of operation(create, update, delete): eventType
        * type of object: objectType
        * the object itself: T
        * JSON view for serialization: view

    If we have deal with a different classes, we need a set of senders(save it in the register) or we can use Consumer

     */
    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view) {
        ObjectWriter writer = mapper
                .setConfig(mapper.getDeserializationConfig())
                .writerWithView(view);
        return (EventType eventType, T payload) -> {
            String value = null;
            try {
                value = writer.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            template.convertAndSend(
                    "/topic/activity",
                    new WsEventDto(objectType, eventType, value)
            );
        };
    }
}
