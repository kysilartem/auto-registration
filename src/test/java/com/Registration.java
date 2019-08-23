package com;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.*;

public class Registration{

    private final String GMAIL_POP = "pop.gmail.com";
    private final String EMAIL_PASSWORD ="27cc4949d6104a59a0deb2433caaeb573c520aaf";


    @Test (priority = 0)
    public void fillingTheForm() throws IOException {

        Credentials.initCredentials();

        open("https://adminstage5.quickblox.com");
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
        $(By.xpath("//*[@id=\"wrap\"]/p[1]")).shouldBe(Condition.visible);

    }

    @Test(dependsOnMethods = "fillingTheForm" )
    public void confirmRegistration() {

        AccountConfirmation.checkNewMail(GMAIL_POP, Credentials.getEmail(), EMAIL_PASSWORD);
        open(AccountConfirmation.link);

    }

}
