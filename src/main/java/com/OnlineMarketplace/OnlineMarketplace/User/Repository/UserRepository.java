package com.OnlineMarketplace.OnlineMarketplace.User.Repository;

import com.OnlineMarketplace.OnlineMarketplace.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
