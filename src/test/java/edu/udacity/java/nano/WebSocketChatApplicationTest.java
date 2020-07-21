package edu.udacity.java.nano;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { WebSocketChatApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketChatApplicationTest {
    private WebDriver webDriver;
    private WebDriver webDriver2;
    private String homeUrl = "http://localhost:8080";

    @Test
    public void testLoginAndJoin() {
        webDriver = new HtmlUnitDriver(true);
        webDriver.get(homeUrl);

        String inputName = "Hello world!";

        WebElement username = webDriver.findElement(By.name("username"));
        username.sendKeys(inputName);

        WebElement submit = webDriver.findElement(By.className("submit"));
        submit.click();

        assertThat(webDriver.findElement(By.id("username")).getAttribute("innerHTML")).isEqualTo(inputName);
    }

    @Test
    public void testChatAndLeave() {
        webDriver  = new HtmlUnitDriver(true);
        webDriver2 = new HtmlUnitDriver(true);

        String username = "test1";
        webDriver.get(homeUrl + "/index?username=" + username);

        String username2 = "test2";
        webDriver2.get(homeUrl + "/index?username=" + username2);

        WebElement sendText = webDriver.findElement(By.id("msg"));
        sendText.sendKeys("Lorem ipsum dolor sit amet..");

        WebElement sendButton = webDriver.findElement(By.className("send"));
        sendButton.click();

        WebDriverWait wait = new WebDriverWait(webDriver2, 30);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("message-content")));
        System.out.println(element.getAttribute("textContent").length());
        assertThat(element.getAttribute("textContent").substring(4, 18)).isEqualTo("Dolor sit amet..");

        WebElement leaveChat = webDriver.findElement(By.className("exit"));
        leaveChat.click();

        assertThat(webDriver.findElement(By.className("submit")).getText().equals("login"));
    }
}
