package com.git.stavrosdim.sw.simplewebsite.POMs;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.git.stavrosdim.sw.simplewebsite.POJOs.User;

public class DisplayUsersPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators

    // Items
    private static By modal = By.xpath("//app-info-modal");
    private By toastContainer = By.id("toast-container");
    private By toastTitle = By.xpath(".//div[contains(@class,'toast-title')]");

    // Buttons
    private static By deleteButton = By.id("modal-button-delete");
    private By editSaveButton = By.id("modal-button-edit-save");

    // Input Fields
    private By inputName = By.id("modal-form-input-name");
    private By inputSurname = By.id("modal-form-input-surname");
    private By inputGender = By.id("modal-form-input-gender");
    private By inputBirthdate = By.id("modal-form-input-birthdate");
    private By inputHomeAddress = By.id("modal-form-input-homeAddress");
    private By inputWorkAddress = By.id("modal-form-input-workAddress");

    // Plain Text Fields
    private By name = By.id("modal-form-name");
    private By surname = By.id("modal-form-surname");
    private By gender = By.id("modal-form-gender");
    private By birthdate = By.id("modal-form-birthdate");
    private By homeAddress = By.id("modal-form-homeAddress");
    private By workAddress = By.id("modal-form-workAddress");

    public DisplayUsersPage(WebDriver driver, String displayUsersUrl) {
        this.driver = driver;
        this.driver.get(displayUsersUrl);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public WebElement openModalForUser(String username) {
        try {
            final var userRow = wait.until(ExpectedConditions.elementToBeClickable(
                    findUserOnTable(username)));
            if (userRow != null) {
                userRow.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(modal));
            }
            return userRow;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean checkModalClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(modal));
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDeleteButton() {
        driver.findElement(deleteButton).click();
    }

    public void clickEditSaveButton() {
        driver.findElement(editSaveButton).click();
    }

    public void editData(User user) {
        clearRow();
        setFieldText(inputName, user.getName());
        setFieldText(inputSurname, user.getSurname());
        new Select(driver.findElement(inputGender)).selectByVisibleText(user.getGender());
        setFieldText(inputBirthdate, user.getBirthdate());
        setFieldText(inputWorkAddress, user.getWorkAddress());
        setFieldText(inputHomeAddress, user.getHomeAddress());
    }

    public User getEditUser() {
        return new User(getItemText(name),
                getItemText(surname),
                getItemText(gender),
                getItemText(birthdate),
                getItemText(workAddress),
                getItemText(homeAddress));
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

    public boolean checkEditSaveButtonEnable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(editSaveButton));
        } catch (Exception e) {
            return false;
        }
        return driver.findElement(editSaveButton).isEnabled();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public boolean checkDeletionFromTable(String username) {
        try {
            return wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(
                            findUserOnTable(username)));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkButtonTextSave() {
        return getItemText(editSaveButton).equals("Save");
    }

    public boolean checkButtonTextEdit() {
        return getItemText(editSaveButton).equals("Edit");
    }

    // Helper methods.

    public static By findUserOnTable(String username) {
        return By.xpath("//tbody[@id='display-users-table']//td[text()='" + username + "']");
    }

    public static By getModal() {
        return modal;
    }

    public static By getModalDeleteButton() {
        return deleteButton;
    }

    private void clearRow() {
        deleteCell(inputName);
        deleteCell(inputSurname);
        driver.findElement(inputBirthdate).clear();
        deleteCell(inputWorkAddress);
        deleteCell(inputHomeAddress);
    }

    private void deleteCell(By field) {
        final var cell = driver.findElement(field);
        cell.click();
        cell.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        cell.sendKeys(Keys.BACK_SPACE);
    }

    private String getItemText(By field) {
        return driver.findElement(field).getText();
    }

    private void setFieldText(By field, String text) {
        driver.findElement(field).sendKeys(text);
    }
}
