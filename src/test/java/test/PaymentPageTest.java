package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import data.Card;
import data.DbUtils;
import page.StartPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static data.DataGenerator.*;
import static data.DataGenerator.getValidName;

public class PaymentPageTest {

    @BeforeAll
    static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        DbUtils.clearTables();
    }

    @AfterAll
    static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @Test
    @Order(1)
    void buyInPaymentGate() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    @Test
    @Order(2)
    void buyInPaymentGateWithDeclinedCardNumber() {
        Card card = new Card(getDeclinedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.checkDeclineNotification();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }

    @Test
    @Order(3)
    void buyInPaymentGateWithInvalidCardNumber() {
        Card card = new Card(getInvalidCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.checkDeclineNotification();
    }

    @Test
    @Order(4)
    void buyInPaymentGateWithShortCardNumber() {
        Card card = new Card(getShortCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Неверный формат");
    }

    @Test
    @Order(5)
    void buyInPaymentGateWithEmptyCardNumber() {
        Card card = new Card(null, getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(6)
    void buyInPaymentGateWithInvalidMonth() {
        Card card = new Card(getApprovedNumber(), "00", getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Неверно указан срок действия карты");
    }

    @Test
    @Order(7)
    void buyInPaymentGateWithNonExistingMonth() {
        Card card = new Card(getApprovedNumber(), "13", getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Неверно указан срок действия карты");
    }

    @Test
    @Order(8)
    void buyInPaymentGateWithExpiredMonth() {
        Card card = new Card(getApprovedNumber(), getLastMonth(), getCurrentYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Неверно указан срок действия карты");
    }

    @Test
    @Order(9)
    void buyInPaymentGateWithEmptyMonth() {
        Card card = new Card(getApprovedNumber(), null, getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(10)
    void buyInPaymentGateWithExpiredYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getLastYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Истёк срок действия карты");
    }

    @Test
    @Order(11)
    void buyInPaymentGateWithEmptyYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), null, getValidName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(12)
    void buyInPaymentGateWithOnlyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Введите полное имя и фамилию");
    }

    @Test
    @Order(13)
    void buyInPaymentGateWithOnlySurname() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlySurname(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Введите полное имя и фамилию");
    }

    @Test
    @Order(14)
    void buyInPaymentGateWithTooLongName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getTooLongName(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Значение поля не может содержать более 100 символов");
    }

    @Test
    @Order(15)
    void buyInPaymentGateWithDigitsInName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithNumbers(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Значение поля может содержать только буквы и дефис");
    }

    @Test
    @Order(16)
    void buyInPaymentGateWithTooShortName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithOneLetter(), getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Значение поля должно содержать больше одной буквы");
    }

    @Test
    @Order(17)
    void buyInPaymentGateWithEmptyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), null, getValidCvc());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(18)
    void buyInPaymentGateWithOneDigitInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithOneDigit());
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Неверный формат");
    }

    @Test
    @Order(19)
    void buyInPaymentGateWithEmptyCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), null);
        val startPage = new StartPage();
        val paymentPage = startPage.buy();
        paymentPage.fulfillData(card);
        paymentPage.findErrorMessage("Поле обязательно для заполнения");
    }
}