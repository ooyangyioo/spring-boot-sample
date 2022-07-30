package org.yangyi.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.vo.QuartzVO;

@RestController
@RequestMapping("/quartz")
public class QuartzController {

    @GetMapping("/string")
    public String string(@RequestParam String name) {
        return "Hello " + name;
    }

    @GetMapping("/entity")
    public QuartzVO entity(@RequestParam String name) {
        return new QuartzVO() {{
            setName("Hello " + name);
        }};
    }

}
