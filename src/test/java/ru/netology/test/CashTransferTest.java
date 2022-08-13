package ru.netology.test;

import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferCashPage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CashTransferTest {

    @BeforeEach
    void form() {
        open("http://localhost:7777");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        loginPage.validLogin(authInfo).validVerify(verificationCode);
    }

    @Test
    void shouldTransferCashFromFirstCard() {

        int amount = 50;
        var cardInfo = DataHelper.getFirstCardNumber();

        var dashboard = new DashboardPage();
        int balanceFirstCard = dashboard.getCardBalance("0");
        int balanceSecondCard = dashboard.getCardBalance("1");

        dashboard.changeCard(1).shouldCashInfo(cardInfo, amount);
        int finalBalanceFirstCard = dashboard.getCardBalance("0");
        int finalBalanceSecondCard = dashboard.getCardBalance("1");

        assertTrue(finalBalanceFirstCard > 0 && finalBalanceSecondCard > 0);

        assertEquals(finalBalanceFirstCard, (balanceFirstCard - amount));
        assertEquals(finalBalanceSecondCard, (balanceSecondCard + amount));
    }

    @Test
    void shouldTransferCashFromSecondCard() {

        int amount = 50;
        var cardInfo = DataHelper.getSecondCardNumber();


        var dashboard = new DashboardPage();
        int finalBalanceFirstCard = dashboard.getCardBalance("1");
        int finalBalanceSecondCard = dashboard.getCardBalance("0");

        dashboard.changeCard(0).shouldCashInfo(cardInfo, amount);
        int balanceFirstCard = dashboard.getCardBalance("1");
        int balanceSecondCard = dashboard.getCardBalance("0");

        assertTrue(finalBalanceFirstCard > 0 && finalBalanceSecondCard > 0);

        assertEquals(finalBalanceFirstCard, balanceFirstCard + amount);
        assertEquals(finalBalanceSecondCard, balanceSecondCard - amount);
    }

    @Test
    void shouldTransferNegativeBalance() {

        int amount = 50000;
        var cardInfoFirst = DataHelper.getFirstCardNumber();
        var cardInfoSecond = DataHelper.getSecondCardNumber();

        var dashboard = new DashboardPage();
        dashboard.changeCard(1).shouldCashInfo(cardInfoFirst, amount);
        int finalBalanceFirstCard = dashboard.getCardBalance("0");
        int finalBalanceSecondCard = dashboard.getCardBalance("1");

        var transfer = new TransferCashPage();
        if (finalBalanceFirstCard < 0) {
            transfer.noNegativeBalance(cardInfoFirst);
        } else if (finalBalanceSecondCard < 0) {
            transfer.noNegativeBalance(cardInfoSecond);
        }
    }
}