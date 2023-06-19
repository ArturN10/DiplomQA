package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditPageTest {
    @BeforeAll
    static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        DbUtils.clearTables();
        String url = System.getProperty("sut.url");
        open(url);

    }

    @AfterAll
    static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @Test
    @Order(1)
    void buyInCreditGate() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getCreditStatus());
    }

    @Test
    @Order(2)
    void buyInCreditGateWithDeclinedCardNumber() {
        Card card = new Card(getDeclinedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.checkDeclineNotification();
    }

    @Test
    @Order(3)
    void buyInCreditGateWithInvalidCardNumber() {
        Card card = new Card(getInvalidCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.checkDeclineNotification();
    }

    @Test
    @Order(4)
    void buyInCreditGateWithShortCardNumber() {
        Card card = new Card(getShortCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Неверный формат");
    }

    @Test
    @Order(5)
    void buyInCreditGateWithEmptyCardNumber() {
        Card card = new Card(null, getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(6)
    void buyInCreditGateWithInvalidMonth() {
        Card card = new Card(getApprovedNumber(), "00", getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Неверно указан срок действия карты");
    }

    @Test
    @Order(7)
    void buyInCreditGateWithNonExistingMonth() {
        Card card = new Card(getApprovedNumber(), "13", getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Неверно указан срок действия карты");
    }

    @Test
    @Order(8)
    void buyInCreditGateWithExpiredMonth() {
        Card card = new Card(getApprovedNumber(), getLastMonth(), getCurrentYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Истёк срок действия карты");
    }

    @Test
    @Order(9)
    void buyInCreditGateWithEmptyMonth() {
        Card card = new Card(getApprovedNumber(), null, getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(10)
    void buyInCreditGateWithExpiredYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getLastYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Истёк срок действия карты");
    }

    @Test
    @Order(11)
    void buyInCreditGateWithEmptyYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), null, getValidName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(12)
    void buyInCreditGateWithOnlyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Введите полное имя и фамилию");
    }

    @Test
    @Order(13)
    void buyInCreditGateWithOnlySurname() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlySurname(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Введите полное имя и фамилию");
    }

    @Test
    @Order(14)
    void buyInCreditGateWithTooLongName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getTooLongName(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Значение поля не может содержать более 100 символов");
    }

    @Test
    @Order(15)
    void buyInCreditGateWithDigitsInName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithNumbers(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Значение поля может содержать только буквы и дефис");
    }

    @Test
    @Order(16)
    void buyInCreditGateWithTooShortName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithOneLetter(), getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Значение поля должно содержать больше одной буквы");
    }

    @Test
    @Order(17)
    void buyInCreditGateWithEmptyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), null, getValidCvc());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Поле обязательно для заполнения");
    }

    @Test
    @Order(18)
    void buyInCreditGateWithOneDigitInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithOneDigit());
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Значение поля должно содержать 3 цифры");
    }

    @Test
    @Order(19)
    void buyInCreditGateWithEmptyCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), null);
        val startPage = new StartPage();
        val creditPage = startPage.buyInCredit();
        creditPage.fulfillData(card);
        creditPage.findErrorMessage("Поле обязательно для заполнения");
    }
}