package br.com.itstoony.github.api.attornatus.repository;

import br.com.itstoony.github.api.attornatus.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
