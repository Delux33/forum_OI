package com.forum.oi.controller;

import com.forum.oi.domain.Message;
import com.forum.oi.domain.Role;
import com.forum.oi.domain.User;
import com.forum.oi.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
public class TopicController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/topics")
    public String topic(Map<String, Object> model) {

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);

        return "topic";
    }

    @PostMapping("/topics")
    public String addTopic(@AuthenticationPrincipal User user,
                           @RequestParam String text,
                           Map<String, Object> model) {

        Message message = new Message(text, user);

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);

        return "topic";
    }

    @GetMapping("/topics/edit/{message}")
    public String editTopic(@AuthenticationPrincipal User currentUser,
                            @PathVariable Message message,
                            Map<String, Object> model) {

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);
        model.put("message", message);
        model.put("isCurrentUser", currentUser.equals(message.getAuthor()));

        return "editArticle";
    }

    @PostMapping("/topics/edit/{messageId}")
    public String saveChangedTopic(@AuthenticationPrincipal User currentUser,
                                   @RequestParam Message message,
                                   @RequestParam String topic,
                                   Map<String, Object> model) {


        if (message.getAuthor().equals(currentUser) || currentUser.isAdmin()) {
            if (StringUtils.hasText(topic)) {
                message.setText(topic);
            }

            messageRepo.save(message);
        }

        model.put("message", message);

        return "redirect:/topics";
    }

    @GetMapping("/topics/delete/{messageId}")
    public String deleteTopic(@AuthenticationPrincipal User currentUser,
                                   @RequestParam Message message,
                                   @RequestParam String topic,
                                   Map<String, Object> model) {


        if (message.getAuthor().equals(currentUser) || currentUser.isAdmin()) {
            if (StringUtils.hasText(topic)) {
                message.setText(topic);
            }

            messageRepo.save(message);
        }

        model.put("message", message);

        return "redirect:/topics";
    }
}
