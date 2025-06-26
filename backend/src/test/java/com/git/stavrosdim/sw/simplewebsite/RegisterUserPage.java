package com.git.stavrosdim.sw.simplewebsite;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterUserPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public RegisterUserPage(WebDriver driver, String registrationUrl) {
        this.driver = driver;
        this.driver.get(registrationUrl);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void fillForm(User user) {
        driver.findElement(By.id("form-name")).sendKeys(user.getName());
        driver.findElement(By.id("form-surname")).sendKeys(user.getSurname());
        new Select(driver.findElement(By.id("form-gender"))).selectByValue(user.getGender());
        driver.findElement(By.id("form-birthdate")).sendKeys(user.getBirthdate());
        driver.findElement(By.id("form-workAddress")).sendKeys(user.getWorkAddress());
        driver.findElement(By.id("form-homeAddress")).sendKeys(user.getHomeAddress());
    }

    public boolean checkSubmitButtonEnable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("button-submit")));
        } catch (Exception e) {
            return false;
        }
        return driver.findElement(By.id("button-submit")).isEnabled();
    }

    public void clickSubmitButton() {
        driver.findElement(By.id("button-submit")).click();
    }

    public boolean checkNotificationMessageSuccess() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));
        } catch (Exception e) {
            return false;
        }
        final var notificationTitle = driver.findElement(By.id("toast-container"))
                .findElement(By.xpath(".//div[contains(@class,'toast-title')]"));
        return notificationTitle.getText().equals("Success");
    }

    public boolean checkUserOnTable(String username) {

        driver.findElement(By.id("home-button")).click();
        driver.findElement(By.id("display-button")).click();
        driver.navigate().refresh();
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//tbody[@id='display-users-table']//td[text()='" + username + "']")));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void clearNewUser(String username) {
        driver.findElement(By.xpath("//tbody[@id='display-users-table']//td[text()='" + username + "']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-info-modal")));
        driver.findElement(By.id("modal-button-delete")).click();
    }
}
