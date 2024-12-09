package lv.dsns.support24.nabatmessage.repository;

import jakarta.validation.constraints.NotNull;
import lv.dsns.support24.nabatmessage.repository.entity.NabatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NabatMessageRepository extends JpaRepository<NabatMessage, UUID> {

    boolean existsNabatMessageByNabatMessage(@NotNull(message = "Nabat message is required field!") String nabatMessage);

}
