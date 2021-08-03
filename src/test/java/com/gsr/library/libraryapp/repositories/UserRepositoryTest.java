package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    private final UserRepository testUserRepository;

    @Autowired
    public UserRepositoryTest(UserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    @Test
    void checkForAUserThatExistsByEmail() {
        //given
        String email = "gayal@domain.com";
        User u1 = new User("Gayal", "Rupasinghe", email);
        testUserRepository.save(u1);
        //when
        boolean userExists = testUserRepository.checkIfUserExistByEmail(email);
        //then
        assertThat(userExists).isTrue();
    }

    @Test
    void checkForAUserThatDoesNotExistByEmail() {
        //given
        String email = "gayal@domain.com";
        //when
        boolean userExists = testUserRepository.checkIfUserExistByEmail(email);
        //then
        assertThat(userExists).isFalse();
    }

    @Test
    void checkForAUserThatExistsByID() {
        //given
        User u1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        testUserRepository.save(u1);
        //when
        boolean userExists = testUserRepository.checkIfUserExistsByID(u1.getUserID());
        //then
        assertThat(userExists).isTrue();
    }

    @Test
    void checkForAUserThatDoesNotExistByID() {
        //given
        Long userID = 1L;
        //when
        boolean userExists = testUserRepository.checkIfUserExistsByID(userID);
        //then
        assertThat(userExists).isFalse();
    }
}