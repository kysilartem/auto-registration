package com;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.*;

public class Registration{

    private final String GMAIL_POP = "pop.gmail.com";
    private final String EMAIL_PASSWORD ="27cc4949d6104a59a0deb2433caaeb573c520aaf";



    public void fillingTheForm() throws IOException, InterruptedException {

        System.out.println("HELLO Registration");
        Credentials.initCredentials();

        open(Credentials.getUrl());
        $(By.xpath("//*[@id=\"login-block\"]/div/div/div/p/a")).click();
        $(By.xpath("//*[@id=\"reg-block\"]/div/div/div/h3")).shouldBe(Condition.visible);

        $(By.xpath("//*[@id=\"user_full_name\"]")).setValue(Credentials.getFullName());
        $(By.xpath("//*[@id=\"user_email\"]")).setValue(Credentials.getEmail());
        $(By.xpath("//*[@id=\"user_login\"]")).setValue(Credentials.getLogin());
        $(By.xpath("//*[@id=\"user_password\"]")).setValue(Credentials.getPassword());
        $(By.xpath("//*[@id=\"user_password_confirmation\"]")).setValue(Credentials.getPassword());
        $(By.xpath("//*[@id=\"user_registration_code\"]")).setValue(Credentials.getRegCode());
        $(By.xpath("//*[@id=\"signup_terms\"]")).click();
        $(By.xpath("//*[@id=\"signup_submit\"]")).click();

        Thread.sleep(1500);

        System.out.println($(By.xpath("//*[@id=\"wrap\"]/div")).innerHtml());
        System.out.println($(By.cssSelector("//*[@id=\"wrap\"]/div")).getSelectedText());
        System.out.println($(By.cssSelector("#wrap > div")).text());
        System.out.println($(By.cssSelector("#wrap > div")).innerText());
        System.out.println($(By.cssSelector("#wrap > div")).getText());
        System.out.println($(By.xpath("//p[contains(@class, \"alert alert-danger\")]")));
    }


    public void confirmRegistration() {

        AccountConfirmation.checkNewMail(GMAIL_POP, Credentials.getEmail(), EMAIL_PASSWORD);
        open(AccountConfirmation.link);

    }

}
