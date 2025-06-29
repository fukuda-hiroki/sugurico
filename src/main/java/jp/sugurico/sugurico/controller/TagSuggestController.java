package jp.sugurico.sugurico.controller;

import jp.sugurico.sugurico.entity.TagDicEntity;
import jp.sugurico.sugurico.repository.TagDicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
public class TagSuggestController {

    @Autowired
    private TagDicRepository tagDicRepository;


    @GetMapping("/suggest")
    public List<String> suggestTags(@RequestParam("query") String query,
                                    @RequestParam(value = "exclude", required = false) List<String> excludeTags) {
        //  もし検索キーワードが空なら空のリストを返す
        if (query.isEmpty()) {
            return Collections.emptyList();
        }
        //  DBから前方一致でタグを検索
        List<TagDicEntity> tags = tagDicRepository.findByTagNameStartingWith(query);

        // 除外リストが指定されていれば、フィルタリングする
        if(excludeTags != null && !excludeTags.isEmpty()) {
            return tags.stream()
                    .map(TagDicEntity::getTagName)  // 除外リストに含まれていないものだけを残す
                    .filter(tagName -> !excludeTags.contains(tagName))
                    .limit(10)
                    .collect(Collectors.toList());
        }

        // 除外リストがなければ、そのまま返す
        return tags.stream()
                .map(TagDicEntity::getTagName)
                .limit(10)
                .collect(Collectors.toList());
    }

}
