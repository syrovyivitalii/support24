package lv.dsns.support24.nabat.repository;

import lv.dsns.support24.nabat.repository.entity.Nabat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NabatRepository extends JpaRepository<Nabat, UUID> {
}
