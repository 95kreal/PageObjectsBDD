package ru.netology.page;

import ru.netology.data.DataHelper;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeVerification = $("[data-test-id='code'] input");
    private SelenideElement buttonVerification = $("[data-test-id='action-verify']");

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeVerification.setValue(verificationCode.getCode());
        buttonVerification.click();
        return new DashboardPage();
    }
}