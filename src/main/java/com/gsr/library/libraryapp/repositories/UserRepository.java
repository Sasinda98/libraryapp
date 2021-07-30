package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
