package com.devrezaur.repository;

import com.devrezaur.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsernameIgnoreCase(String username);

    User findUserByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET image_url=?2 WHERE user_id=?1", nativeQuery = true)
    void updateImageUrl(String userId, String imageUrl);
}
