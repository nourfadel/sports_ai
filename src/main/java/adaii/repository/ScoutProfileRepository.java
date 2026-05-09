package adaii.repository;

import adaii.entity.ScoutProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoutProfileRepository extends JpaRepository<ScoutProfile, Long> {

    Optional<ScoutProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}