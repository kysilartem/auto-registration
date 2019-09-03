package com;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class AppConfiguring {

    private static final String APP_NAME = "App4Jenkins";

    private static final String FB_ID = "513412362439250";
    private static final String FB_SECRET = "c2f8634dafa73dce9804d3e1547752a9";
    private static final String TWITTER_KEY = "ENQAI9CTIsyf3jlVxbCRC02rd";
    private static final String TWITTER_SECRET = "YO7W7ZXCxzJiOk9ADFi19fzxaZRiF8O7mGG4LvYYBIkYccLSN5";

    private  static final String IMG_PATH = "./src/test/resources/ninja-simple-512.png";
    private static final String APNS_CERTIFICATE_PATH = "./src/test/resources/TestAppPushNotificationCertificates.p12";
    private static final String APNS_CERTIFICATE_PASSWORD = "quickblox2019";
    private static final String VOIP_CERTIFICATE_PATH = "./src/test/resources/VoipsampleProdCert.p12";
    private static final String VOIP_CERTIFICATE_PASSWORD = "111";
    private static final String GCM_KEY = "AAAAtuvz-_o:APA91bFqHJ_H90fwBaJ1JVlvcRvOnZdCgasXRgpJKK0r5cinAE6bg0D6NifD5VeocdsJSSLJc_R6gWm6EGcdFYeJqTmGPNXyZhc1yAcDyecloV8zlNjuGiHXF09v7-0gXvQT5slgWjgO";

    private boolean check = false;


    @Test
    public void registration () throws IOException, InterruptedException {
        Registration reg = new Registration();
        reg.fillingTheForm();
    }

    @Test (dependsOnMethods = "registration" )
    public void checkThatAccountExists () throws InterruptedException {

        Thread.sleep(3500);

        if ($(byXpath("//*[@id=\"wrap\"]/p[1]")).exists()) {
            check = true;
            close();

        } else if ($(byXpath("//*[@id=\"wrap\"]/div")).exists()) {
            System.out.println("\n");
            System.out.println($(byXpath("//*[@id=\"wrap\"]/div")).innerHtml());
            System.out.println("\n");
        }

    }

    @Test (dependsOnMethods = "checkThatAccountExists")
    public void confirmRegistration(){

        if (check) {
            Registration reg = new Registration();
            reg.confirmRegistration();
        }
        else {
            Assert.fail("ACCOUNT ALREADY EXIST");
        }
    }

//    @Test (dependsOnMethods = "confirmRegistration")
//    public void login(){
//
//    //open(Credentials.getUrl());
//    $(byXpath("//*[@id=\"user_login\"]")).setValue(Credentials.getLogin());
//    $(byXpath("//*[@id=\"user_password\"]")).setValue(Credentials.getPassword());
//    $(byXpath("//*[@id=\"signin_submit\"]")).click();
//
//    }

    @Test (dependsOnMethods = "confirmRegistration")
    public void createApp() throws InterruptedException {

    $(byXpath("//*[@id=\"workspace\"]/div/div/div/div/ul/li/a")).click();
    $(byXpath("//*[@id=\"application_title\"]")).setValue(APP_NAME);
    $(byXpath("//*[@id=\"avatar_file\"]")).sendKeys(Paths.get(IMG_PATH).toAbsolutePath().toString());
    $(byXpath("//*[@id=\"facebook_key\"]")).setValue(FB_ID);
    $(byXpath("//*[@id=\"facebook_secret\"]")).setValue(FB_SECRET);
    $(byXpath("//*[@id=\"twitter_key\"]")).setValue(TWITTER_KEY);
    $(byXpath("//*[@id=\"twitter_secret\"]")).setValue(TWITTER_SECRET);
    $(byXpath("//*[@id=\"form-add-app\"]/div[4]/div/div/button")).click();

    close();
    }

    @Test (dependsOnMethods = "createApp")
    public void uploadCertificates() throws InterruptedException {

        open(Credentials.getUrl() + "/apps");
        $(byXpath("//*[@id=\"user_login\"]")).setValue(Credentials.getLogin());
        $(byXpath("//*[@id=\"user_password\"]")).setValue(Credentials.getPassword());
        $(byXpath("//*[@id=\"signin_submit\"]")).click();
        //Selenide.refresh();

        $(byAttribute("title",APP_NAME)).waitUntil(Condition.appear,10000).click();
        $(byXpath("//*[@id=\"app-list\"]/a[5]/i")).click();

        $(byXpath("//*[@id=\"workspace\"]/div[1]/div[2]/div[1]/ul/li[4]/a")).click();
        $(byXpath(".//*[@id='upload_cert_password']")).setValue(APNS_CERTIFICATE_PASSWORD);
        $(byXpath("//*[@id=\"upload_cert\"]")).sendKeys(Paths.get(APNS_CERTIFICATE_PATH).toAbsolutePath().toString());
        $(byXpath("//*[@id=\"upload_submit\"]")).click();

        $(byXpath("//*[@id=\"upload_cert_password\"]")).setValue(VOIP_CERTIFICATE_PASSWORD);
        $(byXpath("//*[@id=\"upload_cert\"]")).sendKeys(Paths.get(VOIP_CERTIFICATE_PATH).toAbsolutePath().toString());
        $(byXpath("//*[@id=\"upload_submit\"]")).waitUntil(Condition.exist,2500).click();


        $(byAttribute("data-target","#gcm")).waitUntil(Condition.exist,2500).click();

        $(byXpath("//*[@id=\"gcm_api_key\"]")).shouldBe(Condition.visible).setValue(GCM_KEY);
        $(byXpath("//*[@id=\"google_submit\"]")).click();

        $(byXpath("//*[@id=\"gcm_environment_production\"]")).click();
        $(byXpath("//*[@id=\"gcm_api_key\"]")).shouldBe(Condition.visible).setValue(GCM_KEY);
        $(byXpath("//*[@id=\"google_submit\"]")).click();

    }

    @Test (dependsOnMethods = "uploadCertificates")
    public void setAdditionalOptions () {


        open(Credentials.getUrl() + "/apps");
        $(byAttribute("title",APP_NAME)).click();
        $(byXpath("//*[@id=\"app-list\"]/a[6]")).click();
        $(byXpath("//*[@id=\"list\"]/div[1]/ul/li[3]/a")).click();

        if ($(byXpath("//*[@id=\"users_settings_users_index\"]")).getAttribute("checked")==null){
            $(byXpath("//*[@id=\"users_settings_users_index\"]")).click();
        }
        if($(byXpath("//*[@id=\"users_settings_new_user_push_enable\"]")).getAttribute("checked")==null) {
            $(byXpath("//*[@id=\"users_settings_new_user_push_enable\"]")).click();
        }
        $(byXpath("//*[@id=\"form-add-app\"]/div/div/div[3]/div[2]/div[2]/input")).click();

    }

    @Test(dependsOnMethods = "setAdditionalOptions")
    public void getCredentials(){
        open(Credentials.getUrl() + "/apps");
        $(byAttribute("title",APP_NAME)).click();
        Credentials.initAppId();
        Credentials.initAuthKey();
        Credentials.initSecretKey();
        $(byXpath("//*[@id=\"login_dropdown\"]/img")).click();
        $(byXpath("//*[@id=\"header-menu\"]/div/ul/li[3]/a")).shouldBe(Condition.visible).click();
        Credentials.initApiEndpoint();
        Credentials.initChatEndpoint();
    }

    @Test (dependsOnMethods = "getCredentials")
    public void createJenkinsCreds()  {

        System.out.println("\n");
        System.out.println("*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*"+"\n");
        System.out.println(Credentials.buildJenkinsCredentials()+"\n");
        System.out.println("*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*");
        System.out.println("\n");

    }



}
