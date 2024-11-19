package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildTypePage extends CreateBasePage {
    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";
    public static final SelenideElement errorBuildTypeName = $("#error_buildTypeName");
    private static final String ERROR_BUILD_TYPE_NAME_TEXT = "Build configuration name must not be empty";

    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public void createBuildConfiguration(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
    }

    public void createBuildConfigurationWithoutName() {
        buildTypeNameInput.clear();
        submitButton.click();
    }

    public static void checkErrorBuildTypeName() {
        errorBuildTypeName.shouldHave(Condition.exactText(ERROR_BUILD_TYPE_NAME_TEXT));
    }
}
