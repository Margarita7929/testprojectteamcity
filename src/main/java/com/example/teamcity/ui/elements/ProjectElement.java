package com.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class ProjectElement extends BasePageElement {

    private SelenideElement name;
    private SelenideElement link;
    private SelenideElement button;

    public ProjectElement(SelenideElement element) {
        super(element);
        this.name = $("span[class*='MiddleEllipsis']");
        this.link = $("a");
        this.button = $("button");


    }


}
