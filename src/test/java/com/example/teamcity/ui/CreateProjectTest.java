package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static com.example.teamcity.ui.pages.BasePage.BASE_WAITING;


@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {


    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {

        loginAs(testData.getUser());

        CreateProjectPage.open("_Root")
                .createForm(GIT_URL)
                .setUpProject(testData.getProject().getName(), testData.getBuildType().getName());


        var createdProject = superUserCheckedRequests.<Project>getRequest(Endpoint.PROJECT).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        ProjectPage.open(createdProject.getId()).title.shouldHave(Condition.exactText(testData.getProject().getName()), BASE_WAITING);

        var foundProjects = ProjectsPage.open().getProjects().stream().anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));
        softy.assertTrue(foundProjects);
    }

}
