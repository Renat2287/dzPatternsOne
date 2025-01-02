package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class OrderCardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeAll
    static void tearDownAll() { SelenideLogger.removeListener("allure"); }

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    @DisplayName("redevelopment")

    void redevelopment() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        var planningDay = DataGenerator.generateDate(4, "dd.MM.yyyy");
        var reschedulingDay = DataGenerator.generateDate(7,"dd.MM.yyyy");

        $("[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDay);
        $("[name='name']").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").findBy(text("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDay));
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(reschedulingDay);
        $$("button").findBy(text("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + reschedulingDay))
                .shouldBe(visible);

    }
}