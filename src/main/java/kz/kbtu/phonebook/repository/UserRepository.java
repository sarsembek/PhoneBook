package kz.kbtu.phonebook.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import kz.kbtu.phonebook.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
}
