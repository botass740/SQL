package ru.netology.mysql.test;

import org.junit.jupiter.api.*;
import ru.netology.mysql.data.DataHelper;
import ru.netology.mysql.data.SQLHelper;
import ru.netology.mysql.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.mysql.data.SQLHelper.cleanAuthCodes;
import static ru.netology.mysql.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage;

    @AfterEach
    void teatDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should success login to dashboard with exist login and password from sut test data ")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());


    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldErrorIfRandom() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification if user exist in base and random verification code")
    void shouldGetErrorNotificationIfRandomCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");

    }

}