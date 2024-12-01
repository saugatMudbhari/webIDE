package com.share.user.language;

import com.share.user.language.dto.UserWriteLanguage;
import com.share.user.language.usecase.ChooseLanguageUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/selectLanguage")
public class LanguageController {
    private final ChooseLanguageUseCase chooseLanguageUseCase;

    @Autowired
    public LanguageController(ChooseLanguageUseCase chooseLanguageUseCase) {
        this.chooseLanguageUseCase = chooseLanguageUseCase;
    }


    @PostMapping("/do")
    public String selectLanguage(UserWriteLanguage language) {
        return chooseLanguageUseCase.selectLanguage(language);
    }

}
