package com;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import java.io.IOException;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;

public class Registration{

    private final String GMAIL_POP = "pop.gmail.com";
    private final String EMAIL_PASSWORD ="27cc4949d6104a59a0deb2433caaeb573c520aaf";



    public void fillingTheForm() throws IOException, InterruptedException {

        Credentials.initCredentials();

        open(Credentials.getUrl());
        $(byXpath("//*[@id=\"login-block\"]/div/div/div/p/a")).click();
        $(byXpath("//*[@id=\"reg-block\"]/div/div/div/h3")).shouldBe(Condition.visible);

        $(byXpath("//*[@id=\"user_full_name\"]")).setValue(Credentials.getFullName());
        $(byXpath("//*[@id=\"user_email\"]")).setValue(Credentials.getEmail());
        $(byXpath("//*[@id=\"user_login\"]")).setValue(Credentials.getLogin());
        $(byXpath("//*[@id=\"user_password\"]")).setValue(Credentials.getPassword());
        $(byXpath("//*[@id=\"user_password_confirmation\"]")).setValue(Credentials.getPassword());
        $(byXpath("//*[@id=\"user_registration_code\"]")).setValue(Credentials.getRegCode());
        $(byXpath("//*[@id=\"signup_terms\"]")).click();
        Thread.sleep(1500);
        $(byXpath("//*[@id=\"signup_submit\"]")).click();

    }


    public void confirmRegistration() {

        AccountConfirmation.checkNewMail(GMAIL_POP, Credentials.getEmail(), EMAIL_PASSWORD);
        open(AccountConfirmation.link);

    }

}
