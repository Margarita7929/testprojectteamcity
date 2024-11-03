package com.example.teamcity.ui;


import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.testng.annotations.Test;


import static com.example.teamcity.api.enums.Endpoint.PROJECT;
import static com.example.teamcity.ui.pages.BasePage.BASE_WAITING;


@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUiTest {


    @Test(description = "User should be able to create build type", groups = {"Positive"})

    public void userCreatesBuildType() {
        loginAs(testData.getUser());

        var userCheckRequest = new CheckedRequests((Specifications.authSpec(testData.getUser())));
        userCheckRequest.<Project>getRequest(PROJECT).create(testData.getProject());

        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(GIT_URL)
                .createBuildConfiguration(testData.getBuildType().getName());

        var createdBuildType = superUserCheckedRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("name:" + testData.getBuildType().getName());
        softy.assertNotNull(createdBuildType);

        BuildTypePage.title.shouldHave(Condition.exactText(testData.getBuildType().getName()), BASE_WAITING);
    }

    @Test(description = "User should not be able to create build type", groups = {"Negative"})

    public void userCreatesBuildTypeWithoutName() {
        loginAs(testData.getUser());

        var userCheckRequest = new CheckedRequests((Specifications.authSpec(testData.getUser())));
        userCheckRequest.<Project>getRequest(PROJECT).create(testData.getProject());

        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(GIT_URL)
                .createBuildConfigurationWithoutName();
        CreateBuildTypePage.checkErrorBuildTypeName();


    }
}