package ADAII.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ADAII.entity.PlayerProfile;

import java.util.Optional;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile,Long> {
    Optional<PlayerProfile> findByUserId(Long id);
}
