package kr.co.greendae.controller;

import kr.co.greendae.dto.community.ArticleDTO;
import kr.co.greendae.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ArticleService articleService;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String version;


    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {

        String cate = "acanotice";
        List<ArticleDTO> acanotices = articleService.findAllByCateLimit5(cate);

        cate = "notice";
        List<ArticleDTO> notices = articleService.findAllByCateLimit5(cate);

        cate = "news";
        List<ArticleDTO> news = articleService.findAllByCateLimit5(cate);

        model.addAttribute("acanotices", acanotices);
        model.addAttribute("notices", notices);
        model.addAttribute("news", news);

        return "/index";
    }


}
