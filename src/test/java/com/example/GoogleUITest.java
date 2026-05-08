package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoogleUITest {
    private WebDriver driver;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        String headless = System.getProperty("headless", "false");
        if (Boolean.parseBoolean(headless)) {
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");
        }
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void waitForPageLoadComplete() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(webDriver ->
            ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
        );
    }

    @AfterEach
    void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void openGoogleAndSearch() {
        driver.get("https://www.google.com");
        waitForPageLoadComplete();

        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(webDriver -> webDriver.getTitle() != null && webDriver.getTitle().toLowerCase().contains("google"));

        assertEquals("Google", driver.getTitle(), "Google home page title should be Google");

        WebElement searchBox = driver.findElement(By.name("q"));
        assertTrue(searchBox.isDisplayed(), "Google search box should be visible");

        searchBox.sendKeys("Selenium");

        WebElement suggestionList = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox']")));

        List<WebElement> suggestions = suggestionList.findElements(By.cssSelector("li"));
        assertTrue(!suggestions.isEmpty(), "Search suggestions should appear when typing a keyword");
    }
}
