package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

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
}