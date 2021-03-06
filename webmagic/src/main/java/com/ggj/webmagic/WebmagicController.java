package com.ggj.webmagic;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/19 10:58
 */
@Controller
@Slf4j
public class WebmagicController {


    @Autowired
    private WebmagicService webmagicService;

    @RequestMapping("/tiebatop/{tieBaName}/{size}")
    public String tieBaTop(@PathVariable("tieBaName") String tieBaName, @PathVariable("size") Integer size, Model model)
            throws Exception {
        webmagicService.getTieBaTop(tieBaName, size, model);
        model.addAttribute("name", tieBaName);
        return "tiebatop";
    }

    @RequestMapping("/tiebatop/level/{tieBaName}/")
    public String tieBaTop(@PathVariable("tieBaName") String tieBaName, Model model) throws Exception {
        webmagicService.getLevelCount(tieBaName, model);
        model.addAttribute("name", tieBaName);
        return "tiebatoplevel";
    }

    @RequestMapping("/tieba/img/{tieBaName}/{begin}/{end}")
    public String tieBaTop(Model model, @PathVariable("tieBaName") String tieBaName,
                           @PathVariable("begin") Integer begin, @PathVariable("end") Integer end) throws Exception {
        model.addAttribute("mapData", webmagicService.getTieBaImage(model, tieBaName, begin, end));
        return "tiebaimage";
    }

    /**
     * 增加默认贴吧
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/")
    public String index(Model model) throws Exception {
        log.info("访问主页");
        //只有访问时候才执行定时任务。
        ScheduleTask.execute=true;
        //取
        model.addAttribute("mapData", webmagicService.getTieBaImage(model, null, 0, 2));
        return "tiebaimage";
    }

    /**
     * 首页滚动分页查询
     *
     * @param model
     * @param begin
     * @param end
     * @return
     * @throws Exception
     */
    @RequestMapping("page/{begin}/{end}")
    @ResponseBody
    public String page(Model model, @PathVariable("begin") Integer begin, @PathVariable("end") Integer end) throws Exception {
        log.info("page:{}到{}", begin, end);
        Map<String, List<String>> map = webmagicService.getTieBaImage(model, null, begin, end);
        return  JSONObject.toJSON(map).toString();
    }

    @RequestMapping("/tieba/search")
    public String search(Model model, String keyWord, Integer from) throws Exception {
        log.info("ngingx搜索缓存:{},{}", keyWord, from);
        model.addAttribute("listContent", webmagicService.search(model, keyWord, from));
        model.addAttribute("keyWord", keyWord);
        return "search";
    }
}
