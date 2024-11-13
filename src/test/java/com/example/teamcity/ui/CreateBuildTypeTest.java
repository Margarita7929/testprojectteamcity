package com.example.teamcity.ui;


import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.example.teamcity.api.enums.Endpoint.PROJECT;
import static com.example.teamcity.ui.pages.BasePage.BASE_WAITING;
import static org.testng.Assert.assertEquals;


@Slf4j
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


        var responseFirst = superUserCheckedRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("?locator" + "=project:id:" + testData.getProject().getId());

        String jsonResponseFirst = responseFirst.toString();
        Pattern patternOne = Pattern.compile("\"count\"\\s*:\\s*(\\d+)");
        Matcher matcherOne = patternOne.matcher(jsonResponseFirst);
        int buildTypesCountFirstCheck;
        if (matcherOne.find()) {
            buildTypesCountFirstCheck = Integer.parseInt(matcherOne.group(1));
        } else {
            throw new IllegalStateException("Поле 'count' не найдено в первом ответе.");
        }


        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(GIT_URL)
                .createBuildConfigurationWithoutName();
        CreateBuildTypePage.checkErrorBuildTypeName();


        var responseSecond = superUserCheckedRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("?locator" + "=project:id:" + testData.getProject().getId());

        String jsonResponseSecond = responseSecond.toString();
        Pattern patternTwo = Pattern.compile("\"count\"\\s*:\\s*(\\d+)");
        Matcher matcherTwo = patternTwo.matcher(jsonResponseSecond);
        int buildTypesCountSecondCheck;
        if (matcherTwo.find()) {
            buildTypesCountSecondCheck = Integer.parseInt(matcherTwo.group(1));
        } else {
            throw new IllegalStateException("Поле 'count' не найдено во втором ответе.");
        }

          assertEquals(buildTypesCountFirstCheck, buildTypesCountSecondCheck, "Build types counts do not match");
  System.out.println(buildTypesCountFirstCheck);
  System.out.println(buildTypesCountSecondCheck);


    }
}