package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.MUser;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class MUserServiceImplTest {

    @Mock
    private UserRepository testUserRepository;
    private UserService testUserService;

    @BeforeEach
    void setUp() {
        testUserService = new UserServiceImpl(testUserRepository);
    }

    @Test
    void getUserByID() {
        //given
        Long userID = 1L;

        //when
        testUserService.getUserByID(userID);

        //then
        ArgumentCaptor<Long> userIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testUserRepository).findById(userIDCaptor.capture());

        Long capturedUserID = userIDCaptor.getValue();
        assertThat(capturedUserID).isEqualTo(userID);
    }

    @Test
    void updateUser(){
        //given
        Long userID = 1L;
        MUser MUser = new MUser("Lewis", "Hamilton", "lewis@domain.com");
        MUser.setUserID(userID);

        Optional<MUser> userOptional = Optional.of(MUser);
        given(testUserRepository.findById(userID))
                .willReturn(userOptional);

        //when
        testUserService.updateUser(MUser);

        //then
        ArgumentCaptor<MUser> userCaptor = ArgumentCaptor.forClass(MUser.class);
        verify(testUserRepository).save(userCaptor.capture());

        MUser capturedMUser = userCaptor.getValue();

        assertThat(capturedMUser).isEqualTo(MUser);
    }

    @Test
    void updateUserThrowsOperationStoppedException(){
        //given
        Long userID = 1L;
        MUser MUser = new MUser("Lewis", "Hamilton", "lewis@domain.com");
        MUser.setUserID(userID);

        Optional<MUser> userOptional = Optional.empty();
        given(testUserRepository.findById(userID))
                .willReturn(userOptional);

        //when and then
        assertThatThrownBy(() -> testUserService.updateUser(MUser))
                .isExactlyInstanceOf(NoResourceFoundException.class)
                .hasMessage("User not found to update details.");
    }

    @Test
    void getBooksBorrowedByUserID(){
        //given
        Long userID = 1L;
        MUser MUser = new MUser("Lewis", "Hamilton", "lewis@domain.com");
        MUser.setUserID(userID);

        //when
        testUserService.getBooksBorrowedByUserID(userID);

        //then
        ArgumentCaptor<Long> userIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testUserRepository).getBooksBorrowedByUserID(userIDCaptor.capture());

        Long capturedUserID = userIDCaptor.getValue();
        assertThat(capturedUserID).isEqualTo(userID);
    }

    @Test
    void getUsers() {
        //given
        //when
        testUserService.getUsers();

        //then
        verify(testUserRepository).findAll();
    }
}