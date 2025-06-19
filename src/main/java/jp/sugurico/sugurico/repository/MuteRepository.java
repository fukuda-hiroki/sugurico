package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.MuteEntity;
import jp.sugurico.sugurico.entity.MuteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuteRepository extends JpaRepository<MuteEntity, MuteId> {
}