package org.yangyi.project.controller;

import org.yangyi.project.sender.direct.DirectSender;
import org.yangyi.project.sender.fanout.FanoutSender;
import org.yangyi.project.sender.topic.TopicSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqController {

    private static final Logger logger = LoggerFactory.getLogger(MqController.class);

    @Autowired
    private DirectSender directSender;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private FanoutSender fanoutSender;

    @GetMapping("/direct")
    public void direct() {
        directSender.send();
    }

    @GetMapping("/topic")
    public void topic() {
        topicSender.send();
    }

    @GetMapping("/fanout")
    public void fanout() {
        fanoutSender.send();
    }

}
