package lv.dsns.support24.nabatmessage.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;

@Entity
@Table(name = "tbl_nabat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class NabatMessage extends BaseEntity {

    @Column(name = "message")
    @NotNull(message = "Nabat message is required field!")
    private String nabatMessage;

}
