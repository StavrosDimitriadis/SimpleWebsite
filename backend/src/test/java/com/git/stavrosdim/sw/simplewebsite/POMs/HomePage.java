package com.git.stavrosdim.sw.simplewebsite.POMs;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public HomePage(WebDriver driver, String homeUrl) {
        this.driver = driver;
        this.driver.get(homeUrl);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // Locators

    // Buttons

    private static By homeButton = By.id("home-button");
    private static By displayUsersButton = By.id("display-button");
    private By registerUserButton = By.id("register-button");

    public static By getHomeButton() {
        return homeButton;
    }

    public static By getDisplayUsersButton() {
        return displayUsersButton;
    }
}
