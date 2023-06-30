package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.Card;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private SelenideElement heading = $$("h3").findBy(Condition.text("Кредит по данным карты"));
    private SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("input[placeholder='08']");
    private SelenideElement yearField = $("input[placeholder='22']");
    private SelenideElement holderField = $$("[class='input__control']").get(3);
    private SelenideElement cvcField = $("input[placeholder='999']");
    private SelenideElement continueButton = $$("button").findBy(Condition.text("Продолжить"));


    public CreditPage() {
        heading.shouldBe(Condition.visible);
    }

    public void fulfillData(Card card) {
        cardNumberField.setValue(card.getNumber());
        monthField.setValue(card.getMonth());
        yearField.setValue(card.getYear());
        holderField.setValue(card.getHolderName());
        cvcField.setValue(card.getCvc());
        continueButton.click();

    }
    public void checkSuccessNotification() {
        $(".notification_status_ok").shouldBe(Condition.visible, Duration.ofMillis(20000));
    }

    public void checkDeclineNotification() {
        $("notification_status_error").shouldBe(Condition.visible, Duration.ofMillis(20000));
    }
    public void  findErrorMessage (String text) {
        $(".input__sub").shouldHave(exactText(text)).shouldBe(visible);
    }
}