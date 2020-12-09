package com.smuniov.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.smuniov.sarafan.domain.Message;
import com.smuniov.sarafan.domain.Views;
import com.smuniov.sarafan.repo.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    private int counter = 4;
    private final MessageRepo messageRepo;

    @Autowired
    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
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
        return messageRepo.save(message);
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb,
                          @RequestBody Message message
    ) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message){
       messageRepo.delete(message);
    }
}
