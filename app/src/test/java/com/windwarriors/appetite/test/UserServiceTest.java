package com.windwarriors.appetite.test;

import android.content.Context;

import com.windwarriors.appetite.database.DatabaseManager;
import com.windwarriors.appetite.model.UserModel;
import com.windwarriors.appetite.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.windwarriors.appetite.utils.Constants.tableCreatorString;
import static com.windwarriors.appetite.utils.Constants.tables;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class UserServiceTest {

    private Context context = Robolectric.getShadowApplication().getApplicationContext();

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        // Currently UserService loads initial data with user for all team members
        userService = new UserService(context);
    }

    @After
    public void tearDown() throws Exception {
        userService.close();
    }

    @Test
    public void checkJUnitWorks() {
        assertThat(true, is(true));
    }

    @Test
    public void testRobolectriContext() {
        assertThat(Robolectric.application, notNullValue());
    }

    @Test
    public void getUser_When_EmailAndPasswordAreValid_Then_ReturnNotNullObject() throws Exception {
        String email = "robert.a.argume@gmail.com";
        String password = "123";
        UserModel userFound = userService.getUser(email, password);

        assertNotNull(userFound);
    }

    @Test
    public void getUser_When_EmailAndPasswordAreValid_Then_ReturnedObjectShouldContainSameData() throws Exception {
        String email = "robert.a.argume@gmail.com";
        String password = "123";
        UserModel userFound = userService.getUser(email, password);

        assertEquals(email, userFound.getEmail());
        assertEquals(password, userFound.getPassword());
    }


    @Test
    public void getUser_When_EmailAndPasswordAreNotValid_Then_ReturnNullObject() throws Exception {
        String email = "not_valid@gmail.com";
        String password = "not_valild_password";
        UserModel userFound = userService.getUser(email, password);

        assertNull(userFound);
    }

    @Test
    public void registerUser_When_EmailAlreadyExists_Then_ReturnTrue() throws Exception {
        String email = "preetkamal110@gmail.com";
        String password = "123";

        boolean emailExists = userService.userEmailExists(email);

        assertThat(true, is(emailExists));
    }

    @Test
    public void registerUser_When_RegisteredSuccessfully_Then_ReturnTrue() throws Exception {
        String email = "test_run@test.com";
        String password = "123";

        boolean isRegistered = userService.saveUser(email, password);

        assertThat(true, is(isRegistered));
    }

}