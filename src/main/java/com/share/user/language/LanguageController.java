package com.share.user.language;

import com.share.user.language.dto.UserWriteLanguage;
import com.share.user.language.usecase.ChooseLanguageUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class LanguageController {
    private final ChooseLanguageUseCase chooseLanguageUseCase;

    @Autowired
    public LanguageController(ChooseLanguageUseCase chooseLanguageUseCase) {
        this.chooseLanguageUseCase = chooseLanguageUseCase;
    }


    @PostMapping("/execute-language")
    @CrossOrigin(origins = "*")
    public String selectLanguage(@RequestBody UserWriteLanguage language) {
        return chooseLanguageUseCase.selectLanguage(language);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
