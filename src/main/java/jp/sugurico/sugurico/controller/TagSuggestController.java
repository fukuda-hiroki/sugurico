package jp.sugurico.sugurico.controller;

import jp.sugurico.sugurico.entity.TagDicEntity;
import jp.sugurico.sugurico.repository.TagDicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
public class TagSuggestController {

    @Autowired
    private TagDicRepository tagDicRepository;


    @GetMapping("/suggest")
    public List<String> suggestTags(@RequestParam("query") String query) {
        //  もし検索キーワードが空なら空のリストを返す
        if (query.isEmpty()) {
            return List.of();
        }
        //  DBから前方一致でタグを検索
        List<TagDicEntity> tags = tagDicRepository.findByTagNameStartingWith(query);

        //  TagDicEntityのリストから、tagName(文字列)だけを抜き出して返す
        return tags.stream()
                .map(TagDicEntity::getTagName)
                .collect(Collectors.toList());
    }

}
