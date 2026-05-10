package adaii.repository;

import adaii.entity.ScoutWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoutWatchlistRepository extends JpaRepository<ScoutWatchlist, Long> {

    boolean existsByScoutProfileIdAndPlayerProfileId(Long scoutProfileId, Long playerProfileId);

    Optional<ScoutWatchlist> findByScoutProfileIdAndPlayerProfileId(
            Long scoutProfileId,
            Long playerProfileId
    );

    List<ScoutWatchlist> findByScoutProfileIdOrderByCreatedAtDesc(Long scoutProfileId);
}