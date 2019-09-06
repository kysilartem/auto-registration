package com;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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


//set up headless mode if it's need

    @BeforeClass
    private void setUp(){

        Configuration.driverManagerEnabled = true;
        Configuration.headless = true;

    }

    @Test
    public void registration () throws IOException, InterruptedException {

        //creating a new account
        Registration reg = new Registration();
        reg.fillingTheForm();
    }

    @Test (dependsOnMethods = "registration")
    public void checkThatAccountExists () throws InterruptedException {

        Thread.sleep(3500);

        //check that congratulation message is appear after account registration
        if ($(By.xpath("//*[@id=\"wrap\"]/p[1]")).exists()) {
            check = true;
            close();
        }

        //check that error message is displayed
        else if ($(By.xpath("//*[@id=\"wrap\"]/div")).exists()) {
            System.out.println("\n");
            System.out.println($(By.xpath("//*[@id=\"wrap\"]/div")).innerHtml());
            System.out.println("\n");
        }
    }

    @Test (dependsOnMethods = "checkThatAccountExists")
    public void confirmRegistration(){

        //if congrats message appeared try to confirm registration
        if (check) {
            Registration reg = new Registration();
            reg.confirmRegistration();
        }
        else {
            Assert.fail("ACCOUNT ALREADY EXIST");
        }
    }

    @Test (dependsOnMethods = "confirmRegistration")
    public void createApp() throws InterruptedException {

    //create application and set icon and social network credentials
    $(By.xpath("//*[@id=\"workspace\"]/div/div/div/div/ul/li/a")).click();
    $(By.xpath("//*[@id=\"application_title\"]")).setValue(APP_NAME);
    $(By.xpath("//*[@id=\"avatar_file\"]")).sendKeys(Paths.get(IMG_PATH).toAbsolutePath().toString());
    $(By.xpath("//*[@id=\"facebook_key\"]")).setValue(FB_ID);
    $(By.xpath("//*[@id=\"facebook_secret\"]")).setValue(FB_SECRET);
    $(By.xpath("//*[@id=\"twitter_key\"]")).setValue(TWITTER_KEY);
    $(By.xpath("//*[@id=\"twitter_secret\"]")).setValue(TWITTER_SECRET);
    $(By.xpath("//*[@id=\"form-add-app\"]/div[4]/div/div/button")).click();
    Thread.sleep(1000);
    close();

    }

    @Test (dependsOnMethods = "createApp")
    public void uploadCertificates() {

        //log in the account
        open(Credentials.getUrl() + "/apps");
        $(By.xpath("//*[@id=\"user_login\"]")).setValue(Credentials.getLogin());
        $(By.xpath("//*[@id=\"user_password\"]")).setValue(Credentials.getPassword());
        $(By.xpath("//*[@id=\"signin_submit\"]")).click();
        //Selenide.refresh();

        //open created app
        $(byAttribute("title",APP_NAME)).waitUntil(Condition.appear,10000).click();
        $(By.xpath("//*[@id=\"app-list\"]/a[5]/i")).click();

        //go to PN setting page and upload certificates
        $(By.xpath("//*[@id=\"workspace\"]/div[1]/div[2]/div[1]/ul/li[4]/a")).click();
        $(By.xpath(".//*[@id='upload_cert_password']")).setValue(APNS_CERTIFICATE_PASSWORD);
        $(By.xpath("//*[@id=\"upload_cert\"]")).sendKeys(Paths.get(APNS_CERTIFICATE_PATH).toAbsolutePath().toString());
        $(By.xpath("//*[@id=\"upload_submit\"]")).click();

        $(By.xpath("//*[@id=\"upload_cert_password\"]")).setValue(VOIP_CERTIFICATE_PASSWORD);
        $(By.xpath("//*[@id=\"upload_cert\"]")).sendKeys(Paths.get(VOIP_CERTIFICATE_PATH).toAbsolutePath().toString());
        $(By.xpath("//*[@id=\"upload_submit\"]")).waitUntil(Condition.exist,2500).click();

        //enter the GCM API keys for two environments
        $(byAttribute("data-target","#gcm")).waitUntil(Condition.exist,2500).click();

        $(By.xpath("//*[@id=\"gcm_api_key\"]")).setValue(GCM_KEY);
        $(By.xpath("//*[@id=\"google_submit\"]")).click();

        $(By.xpath("//*[@id=\"gcm_environment_production\"]")).click();
        $(By.xpath("//*[@id=\"gcm_api_key\"]")).setValue(GCM_KEY);
        $(By.xpath("//*[@id=\"google_submit\"]")).click();

    }

    @Test (dependsOnMethods = "uploadCertificates")
    public void setAdditionalOptions () {

        //go to the users setting page
        open(Credentials.getUrl() + "/apps");
        $(byAttribute("title",APP_NAME)).click();
        $(By.xpath("//*[@id=\"app-list\"]/a[6]")).click();
        $(By.xpath("//*[@id=\"list\"]/div[1]/ul/li[3]/a")).click();

        // check the "Allow to retrieve a list of users via API" checkbox if it doesn't checked
        if ($(By.xpath("//*[@id=\"users_settings_users_index\"]")).getAttribute("checked")==null){
            $(By.xpath("//*[@id=\"users_settings_users_index\"]")).click();
        }

        //check the "Enable push notifications for new contact joined from address book" checkbox if it doesn't checked
        if($(By.xpath("//*[@id=\"users_settings_new_user_push_enable\"]")).getAttribute("checked")==null) {
            $(By.xpath("//*[@id=\"users_settings_new_user_push_enable\"]")).click();
        }
        //save changes by the pressing of "Save" button
        $(By.xpath("//*[@id=\"form-add-app\"]/div/div/div[3]/div[2]/div[2]/input")).click();

    }

    @Test(dependsOnMethods = "setAdditionalOptions")
    public void getCredentials(){

        // get application_id \ auth_key \ secret_key
        open(Credentials.getUrl() + "/apps");
        $(byAttribute("title",APP_NAME)).click();
        Credentials.initAppId();
        Credentials.initAuthKey();
        Credentials.initSecretKey();

        //get api and chat endpoints
        $(By.xpath("//*[@id=\"login_dropdown\"]/img")).click();
        $(By.xpath("//*[@id=\"header-menu\"]/div/ul/li[3]/a")).click();
        Credentials.initApiEndpoint();
        Credentials.initChatEndpoint();
    }

    @Test (dependsOnMethods = "getCredentials")
    public void createJenkinsCreds()  {

        //creating and output string with credentials for run api tests via Jenkins
        System.out.println("\n");
        System.out.println("*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*"+"\n");
        System.out.println(Credentials.buildJenkinsCredentials()+"\n");
        System.out.println("*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*");
        System.out.println("\n");

    }
}
