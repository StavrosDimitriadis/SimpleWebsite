package com.git.stavrosdim.sw.simplewebsite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.git.stavrosdim.sw.simplewebsite.configs.FrontendConfig;

@SpringBootTest
@ActiveProfiles("local")
public class SeleniumTests {

    @Autowired
    private FrontendConfig frontendConfig;
    private String registrationUrl;
    private String displayUsersUrl;
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        driver = new FirefoxDriver(options);
        registrationUrl = frontendConfig.getRegisterUserUrl();
        displayUsersUrl = frontendConfig.getDisplayUsersUrl();
    }

    // Registration Page

    @ParameterizedTest
    @MethodSource("validUserDataset")
    public void valid_register_user(User user) {

        // Arrange
        final var registrationPage = new RegisterUserPage(driver, registrationUrl);

        // Act
        registrationPage.fillForm(user);
        assertTrue(registrationPage.checkSubmitButtonEnable(), "Submit button should be enabled");
        registrationPage.clickSubmitButton();

        // Assert
        assertTrue(registrationPage.checkNotificationMessageSuccess(), "Should be success notification");
        assertTrue(registrationPage.checkUserOnTable(user.getName()), "User should be on the table");

        // Clear
        registrationPage.clearNewUser(user.getName());
    }

    @ParameterizedTest
    @MethodSource("invalidUserDataset")
    public void invalid_register_user(User user) {

        // Arrange
        final var registrationPage = new RegisterUserPage(driver, registrationUrl);

        // Act
        registrationPage.fillForm(user);

        // Assert
        assertFalse(registrationPage.checkSubmitButtonEnable(), "Submit button should be disabled");
    }

    // Display Users Page

    @ParameterizedTest
    @MethodSource("validUserDataset")
    public void valid_update_user(User user) {

        // Arrange
        final var testUser = createTestUser();
        final var displayUsersPage = new DisplayUsersPage(driver, displayUsersUrl);

        // Act
        final var row = displayUsersPage.openModalForUser(testUser.getName());
        assertNotNull(row, "Modal should open");
        displayUsersPage.clickEditSaveButton();
        assertTrue(displayUsersPage.checkButtonTextSave(), "Button's text should be 'Save'");
        displayUsersPage.editData(user);
        assertTrue(displayUsersPage.checkEditSaveButtonEnable(), "Save button should be enable");
        displayUsersPage.clickEditSaveButton();
        assertTrue(displayUsersPage.checkButtonTextEdit(), "Button's text should be 'Edit'");

        // Assert
        assertEquals(user, displayUsersPage.getEditUser(), "Updated data should match with input data");
        assertTrue(displayUsersPage.checkNotificationMessageSuccess(), "Should be success notification");
        displayUsersPage.refreshPage();
        final var rowAfterRefresh = displayUsersPage.openModalForUser(user.getName());
        assertNotNull(rowAfterRefresh, "Modal should open");
        assertEquals(user, displayUsersPage.getEditUser(), "Updated data should match with input data");

        // Clear
        displayUsersPage.clickDeleteButton();
    }

    @ParameterizedTest
    @MethodSource("invalidUserDataset")
    public void invalid_update_user(User user) {

        // Arrange
        final var testUser = createTestUser();
        final var displayUsersPage = new DisplayUsersPage(driver, displayUsersUrl);

        // Act
        final var row = displayUsersPage.openModalForUser(testUser.getName());
        assertNotNull(row, "Modal should open");
        displayUsersPage.clickEditSaveButton();
        assertTrue(displayUsersPage.checkButtonTextSave(), "Button's text should be 'Save'");
        displayUsersPage.editData(user);

        // Assert
        assertFalse(displayUsersPage.checkEditSaveButtonEnable(), "Save button should be disabled");

        // Clear
        displayUsersPage.clickDeleteButton();
    }

    @Test
    public void delete_user() {

        // Arrange
        final var testUser = createTestUser();
        final var displayUsersPage = new DisplayUsersPage(driver, displayUsersUrl);

        // Act
        final var row = displayUsersPage.openModalForUser(testUser.getName());

        // Assert
        assertNotNull(row, "Modal should open");
        displayUsersPage.clickDeleteButton();
        assertTrue(displayUsersPage.checkModalClosed(), "Modal should be closed");
        assertTrue(displayUsersPage.checkNotificationMessageSuccess(), "Should be success notification");
        assertTrue(displayUsersPage.checkDeletionFromTable(testUser.getName()), "User should be deleted from table");
        displayUsersPage.refreshPage();
        assertTrue(displayUsersPage.checkDeletionFromTable(testUser.getName()),
                "User should not be loaded in the table");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Datasets

    static Stream<Arguments> validUserDataset() {
        return Stream.of(
                Arguments.of(new User("SeleniumTestName", "SeleniumTestSurname", "M", "2020-05-05",
                        "SeleniumTestWorkAddress", "SeleniumTestHomeAddress")),
                Arguments.of(new User("SeleniumSecName", "SeleniumSecSurname", "F", "2000-02-08", "", "")));
    }

    static Stream<Arguments> invalidUserDataset() {
        return Stream.of(
                Arguments.of(new User("", "Papadopoulos", "M", "2020-05-05", "Karamanli", "")),
                Arguments.of(new User("Christina", "SurnameOver20Characters", "F", "2000-02-08", "",
                        "Andrianoupoleos")),
                Arguments.of(new User("Nikos", "Apostolou", "M", "2030-09-01", "",
                        "")));
    }

    // Helper methods

    private User createTestUser() {
        final var registrationPage = new RegisterUserPage(driver, registrationUrl);
        final var testUser = new User("SeleniumTestBaseName", "SeleniumBaseSurname", "M", "2000-06-10",
                "SeleniumBaseWorkAddress", "SeleniumBaseHomeAddress");
        registrationPage.fillForm(testUser);
        if (registrationPage.checkSubmitButtonEnable()) {
            registrationPage.clickSubmitButton();
        }
        return testUser;
    }
}
