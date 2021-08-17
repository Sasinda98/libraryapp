package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.MUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class MUserRepositoryTest {

    private final UserRepository testUserRepository;

    @Autowired
    public MUserRepositoryTest(UserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    @Test
    void checkForAUserThatExistsByEmail() {
        //given
        String email = "gayal@domain.com";
        MUser u1 = new MUser("Gayal", "Rupasinghe", email);
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

}