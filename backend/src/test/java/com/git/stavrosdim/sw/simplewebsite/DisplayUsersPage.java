package com.git.stavrosdim.sw.simplewebsite;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DisplayUsersPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public DisplayUsersPage(WebDriver driver, String displayUsersUrl) {
        this.driver = driver;
        this.driver.get(displayUsersUrl);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public WebElement openModalForUser(String username) {
        try {
            final var userRow = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//tbody[@id='display-users-table']//td[text()='" + username + "']")));
            if (userRow != null) {
                userRow.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-info-modal")));
            }
            return userRow;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean checkModalClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                    "//app-info-modal")));
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDeleteButton() {
        driver.findElement(By.id("modal-button-delete")).click();
    }

    public void clickEditSaveButton() {
        driver.findElement(By.id("modal-button-edit-save")).click();
    }

    public void editData(User user) {
        clearRow();
        driver.findElement(By.id("modal-form-input-name")).sendKeys(user.getName());
        driver.findElement(By.id("modal-form-input-surname")).sendKeys(user.getSurname());
        new Select(driver.findElement(By.id("modal-form-input-gender"))).selectByVisibleText(user.getGender());
        driver.findElement(By.id("modal-form-input-birthdate")).sendKeys(user.getBirthdate());
        driver.findElement(By.id("modal-form-input-workAddress")).sendKeys(user.getWorkAddress());
        driver.findElement(By.id("modal-form-input-homeAddress")).sendKeys(user.getHomeAddress());
    }

    public User getEditUser() {
        return new User(driver.findElement(By.id("modal-form-name")).getText(),
                driver.findElement(By.id("modal-form-surname")).getText(),
                driver.findElement(By.id("modal-form-gender")).getText(),
                driver.findElement(By.id("modal-form-birthdate")).getText(),
                driver.findElement(By.id("modal-form-workAddress")).getText(),
                driver.findElement(By.id("modal-form-homeAddress")).getText());
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

    public boolean checkEditSaveButtonEnable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("modal-button-edit-save")));
        } catch (Exception e) {
            return false;
        }
        return driver.findElement(By.id("modal-button-edit-save")).isEnabled();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public boolean checkDeletionFromTable(String username) {
        try {
            return wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(
                            By.xpath("//tbody[@id='display-users-table']//td[text()='" + username + "']")));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkButtonTextSave() {
        return (driver.findElement(By.id("modal-button-edit-save")).getText()).equals("Save");
    }

    public boolean checkButtonTextEdit() {
        return (driver.findElement(By.id("modal-button-edit-save")).getText()).equals("Edit");
    }

    private void clearRow() {
        deleteCell("modal-form-input-name");
        deleteCell("modal-form-input-surname");
        driver.findElement(By.id("modal-form-input-birthdate")).clear();
        deleteCell("modal-form-input-workAddress");
        deleteCell("modal-form-input-homeAddress");
    }

    private void deleteCell(String id) {
        final var cell = driver.findElement(By.id(id));
        cell.click();
        cell.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        cell.sendKeys(Keys.BACK_SPACE);
    }
}
