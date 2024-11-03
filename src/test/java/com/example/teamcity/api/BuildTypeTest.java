package com.example.teamcity.api;

import com.example.teamcity.api.generator.TestDataGenerator;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import java.util.Arrays;
import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generator.TestDataGenerator.generate;


@Test(groups = "Regression")
public class BuildTypeTest extends BaseApiTest {
    @Test(description = "User should be able to create build type", groups = {"CRUD", "Positive"})
    public void userCreatesBuildTypeTest() {

        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequest = new CheckedRequests((Specifications.authSpec(testData.getUser())));

        userCheckRequest.<Project>getRequest(PROJECT).create(testData.getProject());

        userCheckRequest.getRequest(BUILD_TYPES).create(testData.getBuildType());


        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "User should not be able to create build type with the same id", groups = {"CRUD", "Negative"})


    public void userCreatesBuildTypeWithSameID() {


        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequest = new CheckedRequests((Specifications.authSpec(testData.getUser())));


        userCheckRequest.<Project>getRequest(PROJECT).create(testData.getProject());


        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());


        userCheckRequest.getRequest(BUILD_TYPES).create(testData.getBuildType());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES).create(buildTypeWithSameId).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));
    }

    @Test(description = "Project Admin should be able to create build type for their project", groups = {"Roles", "Positive"})
    public void projectAdminCreatesBuildTypeTest() {

        superUserCheckedRequests.getRequest(PROJECT).create(testData.getProject());

        testData.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));

        superUserCheckedRequests.<User>getRequest(USERS).create(testData.getUser());

        var userCheckRequest = new CheckedRequests((Specifications.authSpec(testData.getUser())));

        userCheckRequest.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequest.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());

        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "Project Admin should not be able to create build type for not their project", groups = {"Roles", "Negative"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {

        var testDataForUser2 = TestDataGenerator.generate();

        superUserCheckedRequests.getRequest(PROJECT).create(testData.getProject());

        superUserCheckedRequests.getRequest(PROJECT).create(testDataForUser2.getProject());

        testData.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));
        testDataForUser2.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testDataForUser2.getProject().getId()));

        superUserCheckedRequests.<User>getRequest(USERS).create(testData.getUser());

        superUserCheckedRequests.<User>getRequest(USERS).create(testDataForUser2.getUser());

        new CheckedRequests((Specifications.authSpec(testDataForUser2.getUser())));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES).create(testDataForUser2.getBuildType()).then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN).body(Matchers.containsString("Responding with error, status code: 403 (Forbidden)."));
    }
}
