package com.git.stavrosdim.sw.simplewebsite.POMs;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.git.stavrosdim.sw.simplewebsite.POJOs.User;

public class RegisterUserPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators

    // Items
    private By toastContainer = By.id("toast-container");
    private By toastTitle = By.xpath(".//div[contains(@class,'toast-title')]");

    // Buttons
    private By submitButton = By.id("button-submit");

    // Form Fields
    private By name = By.id("form-name");
    private By surname = By.id("form-surname");
    private By gender = By.id("form-gender");
    private By birthdate = By.id("form-birthdate");
    private By homeAddress = By.id("form-homeAddress");
    private By workAddress = By.id("form-workAddress");

    public RegisterUserPage(WebDriver driver, String registrationUrl) {
        this.driver = driver;
        this.driver.get(registrationUrl);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void fillForm(User user) {
        setFieldText(name, user.getName());
        setFieldText(surname, user.getSurname());
        new Select(driver.findElement(gender)).selectByValue(user.getGender());
        setFieldText(birthdate, user.getBirthdate());
        setFieldText(workAddress, user.getWorkAddress());
        setFieldText(homeAddress, user.getHomeAddress());
    }

    public boolean checkSubmitButtonEnable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        } catch (Exception e) {
            return false;
        }
        return driver.findElement(submitButton).isEnabled();
    }

    public void clickSubmitButton() {
        driver.findElement(submitButton).click();
    }

    public boolean checkNotificationMessageSuccess() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(toastContainer));
        } catch (Exception e) {
            return false;
        }
        final var notificationTitle = driver.findElement(toastContainer)
                .findElement(toastTitle);
        return notificationTitle.getText().equals("Success");
    }

    public boolean checkUserOnTable(String username) {

        driver.findElement(HomePage.getHomeButton()).click();
        driver.findElement(HomePage.getDisplayUsersButton()).click();
        driver.navigate().refresh();
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            DisplayUsersPage.findUserOnTable(username)));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void clearNewUser(String username) {
        driver.findElement(DisplayUsersPage.findUserOnTable(username)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(DisplayUsersPage.getModal()));
        driver.findElement(DisplayUsersPage.getModalDeleteButton()).click();
    }

    // Helper methods.

    private void setFieldText(By field, String text) {
        driver.findElement(field).sendKeys(text);
    }
}
