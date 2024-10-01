package com.example.teamcity.api;

import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = "Regression")
public class BuildConfigurationTest extends BaseApiTest{
    @Test(description = "User should be able to create build type", groups = {"CRUD", "Positive"})
    public void userCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Create build type for project by user");
        step("Check build type was successfully created with correct data");
    }

    @Test(description = "User should not be able to create build type with the same id", groups = {"CRUD", "Negative"})
    public void userCreatesBuildTypeWithSameID() {
        step("Create user");
        step("Create project by user");
        step("Create build type 1 for project by user");
        step("Create build type 2 with the same id as build type 1 has for project by user");
        step("Check build type 2 was not created with bad request code");
    }

    @Test(description = "Project Admin should be able to create build type for their project", groups = {"Roles", "Positive"})
    public void projectAdminCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Grand user PROJECT_ADMIN role in the project");
        step("Create build type for project by user - PROJECT_ADMIN");
        step("Check build type was successfully created");
    }

    @Test(description = "Project Admin should not be able to create build type for not their project", groups = {"Roles", "Negative"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create user1");
        step("Create project1 by user1");
        step("Grand user1 PROJECT_ADMIN role in the project1");

        step("Create user2");
        step("Create project1 by user2");
        step("Grand user1 PROJECT_ADMIN role in the project2");

        step("Create build type for project1 by user2");
        step("Check build type was not created with forbidden code");
    }
}
