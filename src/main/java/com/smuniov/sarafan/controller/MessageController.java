package com.smuniov.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.smuniov.sarafan.domain.Message;
import com.smuniov.sarafan.domain.Views;
import com.smuniov.sarafan.dto.EventType;
import com.smuniov.sarafan.dto.ObjectType;
import com.smuniov.sarafan.repo.MessageRepo;
import com.smuniov.sarafan.util.WsSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    @JsonView({Views.IdName.class})
    public List list(){
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView({Views.FullMessage.class})
    public Message getOne(@PathVariable("id") Message message) { //find message entity by id
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message){
        message.setCreationDate(LocalDateTime.now());
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);
        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb,
                          @RequestBody Message message
    ) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message){
       messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);

    }

}
