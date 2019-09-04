package com;

import com.jayway.restassured.path.json.JsonPath;
import org.apache.commons.codec.digest.DigestUtils;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;

public class Credentials {

    private static String instanceName;
    private static String url;
    private static String fullName;
    private static String email;
    private static String login;
    private static String password;
    private static String regCode;

    private static String appId;
    private static String authKey;
    private static String secretKey;
    private static String apiEndpoint;
    private static String chatEndpoint;


    static String getInstanceName() {
        return instanceName;
    }

    static void setInstanceName(String instanceName) {
        Credentials.instanceName = instanceName;
    }

    static String getUrl() {
        return url;
    }

    static void setUrl(String url) {
        Credentials.url = url;
    }

    static String getFullName() {
        return fullName;
    }

    static void setFullName(String fullName) {
        Credentials.fullName = fullName;
    }

    static String getEmail() {
        return email;
    }

    static void setEmail(String email) {
        Credentials.email = email;
    }

    static String getLogin() {
        return login;
    }

    static void setLogin(String login) {
        Credentials.login = login;
    }

    static String getPassword() {
        return password;
    }

    static void setPassword(String password) {
        Credentials.password = password;
    }

    static String getRegCode() {
        return regCode;
    }

    static void setRegCode(String regCode) {
        Credentials.regCode = regCode;
    }

    static String getAppId() {
        return appId;
    }

    static void setAppId(String appId) {
        Credentials.appId = appId;
    }

    static String getAuthKey() {
        return authKey;
    }

    static void setAuthKey(String authKey) {
        Credentials.authKey = authKey;
    }

    static String getSecretKey() {
        return secretKey;
    }

    static void setSecretKey(String secretKey) {
        Credentials.secretKey = secretKey;
    }

    static String getApiEndpoint() {
        return apiEndpoint;
    }

    static void setApiEndpoint(String apiEndpoint) {
        Credentials.apiEndpoint = apiEndpoint;
    }

    static String getChatEndpoint() {
        return chatEndpoint;
    }

    static void setChatEndpoint(String chatEndpoint) {
        Credentials.chatEndpoint = chatEndpoint;
    }


    static void initCredentials() throws IOException {

        //read JSON file
        String credentials = new String(Files.readAllBytes(Paths.get("src/test/resources/credentials.json")));
        JsonPath jsonPath = new JsonPath(credentials);

        //set values to variables
        setInstanceName(jsonPath.getString("instance"));
        setUrl(jsonPath.getString("url"));
        setFullName(jsonPath.getString("fullName"));
        setEmail(jsonPath.getString("email"));
        setLogin(jsonPath.getString("login"));
        setPassword(DigestUtils.sha1Hex (getUrl().substring(8)+"18!new!update"));
        setRegCode(jsonPath.getString("regCode"));

    }


    static void initAppId(){

        setAppId($(By.xpath("//*[@id=\"app_id\"]")).getAttribute("value"));

    }

    static void initAuthKey(){

        setAuthKey($(By.xpath("//*[@id=\"app_auth_key\"]")).getAttribute("value"));

    }

    static void initSecretKey(){

        setSecretKey($(By.xpath("//*[@id=\"app_auth_secret\"]")).getAttribute("value"));

    }

    static void initApiEndpoint() {

        setApiEndpoint($(By.xpath("//*[@id=\"qb_api\"]")).getAttribute("value").substring(8));

    }

    static void initChatEndpoint() {

        setChatEndpoint($(By.xpath("//*[@id=\"chat_api\"]")).getAttribute("value"));

    }


    static String buildJenkinsCredentials(){

        //create string with credentials
        StringBuilder jenkinsCreds = new StringBuilder();
        jenkinsCreds.append(getInstanceName()).append(":").append(apiEndpoint).append(",").append(appId).append(",").append(authKey).append(",").append(secretKey).append(",").append(getLogin()).append(",").append(getPassword()).append(",").append(chatEndpoint);
        return jenkinsCreds.toString();

    }




}
