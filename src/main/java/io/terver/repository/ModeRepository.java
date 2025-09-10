package io.terver.repository;

import io.terver.enums.State;
import io.terver.entity.Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeRepository extends JpaRepository<Mode, Long> {
    Mode findByCallback(String callback);
    Mode findByState(State state);
}
