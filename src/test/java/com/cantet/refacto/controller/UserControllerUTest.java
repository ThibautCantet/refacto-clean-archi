package com.cantet.refacto.controller;

import com.cantet.refacto.service.InvalidFieldException;
import com.cantet.refacto.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
class UserControllerUTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Nested
    class GetUsesShould {
        @Test
        void return_ok_and_existing_users() {
            // given
            final UserModel userModel = new UserModel(null, "toto", "toto@test.com", null, null);
            when(userDAO.getAllUsers()).thenReturn(singletonList(userModel));

            // when
            final ResponseEntity<List<UserModel>> result = userController.getAllUsers();

            // then
            assertThat(result.getBody()).containsOnly(userModel);
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Nested
    class AddUserShould {

        @Test
        void return_ok_and_message() {
            // given
            final UserDto userDto = new UserDto("toto", "toto@test.com");

            // when
            final ResponseEntity<String> result = userController.addUser(userDto);

            // then
            assertThat(result.getBody()).isEqualTo("Test user created");
            assertThat(result.getStatusCode()).isEqualTo(CREATED);
        }

        @Test
        void call_addUser() throws InvalidFieldException {
            // given
            final String name = "toto";
            final String email = "toto@test.com";
            final UserDto userDto = new UserDto(name, email);

            // when
            userController.addUser(userDto);

            // then
            verify(userService).addUser(name, email);
        }
    }

    @Nested
    class UpdateUserShould {

        public static final String USER_ID = "21323234";
        public static final String ORIGINAL_NAME = "pouet";
        public static final String ORIGINAL_EMAIL = "pouet@test.com";
        public final Date CREATED = new Date(2019, 1, 1);
        public final Date ORIGINAL_LAST_CONNECTION = new Date(2019, 1, 1);

        @BeforeEach
        void setUp() {
            final UserModel originalSavedUser = new UserModel(USER_ID, ORIGINAL_NAME, ORIGINAL_EMAIL, CREATED, ORIGINAL_LAST_CONNECTION);
            when(userDAO.getUserById(USER_ID)).thenReturn(originalSavedUser);
        }

        @Test
        void return_ok_and_message() {
            // given
            final UserModel userModel = new UserModel(USER_ID, "toto2", "toto2@test.com", null, null);

            // when
            final ResponseEntity<String> result = userController.updateUser(userModel);

            // then
            assertThat(result.getBody()).isEqualTo("Test user updated");
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void call_userDaoUpdateUser() {
            // given
            final String newName = "toto";
            final String newEmail = "toto@test.com";
            final UserModel newUserModel = new UserModel(USER_ID, newName, newEmail, CREATED, ORIGINAL_LAST_CONNECTION);

            // when
            userController.updateUser(newUserModel);

            // then
            ArgumentCaptor<UserModel> userModelArgumentCaptor = ArgumentCaptor.forClass(UserModel.class);
            verify(userDAO).updateUser(userModelArgumentCaptor.capture());
            final UserModel expectedUser = userModelArgumentCaptor.getValue();
            assertThat(expectedUser.getUserId()).isEqualTo(USER_ID);
            assertThat(expectedUser.getName()).isEqualTo(newName);
            assertThat(expectedUser.getEmail()).isEqualTo(newEmail);
            assertThat(expectedUser.getCreated()).isEqualTo(CREATED);
        }
    }
}