package com.gxh.community.controller;

import com.gxh.community.cache.TagCache;
import com.gxh.community.dto.QuestionDTO;
import com.gxh.community.mapper.QuestionMapper;
import com.gxh.community.mapper.UserMapper;
import com.gxh.community.model.Question;
import com.gxh.community.model.User;
import com.gxh.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionService questionService;
    //编辑功能
    @GetMapping("/publish/{id}")
    public String eidt(@PathVariable("id") Long id,
                        Model model){
        QuestionDTO question = questionService.getQuestionById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.getTag());
        return "publish";
    }
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.getTag());
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id") Long id,
                            HttpServletRequest request,
                            Model model){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.getTag());
        if (title == null || title == "") {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        String invalid = TagCache.filterInVaild(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
