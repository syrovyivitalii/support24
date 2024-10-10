package lv.dsns.support24.nabatgroup.service;

import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;

import java.util.List;
import java.util.UUID;

public interface NabatGroupService {
    NabatGroupResponseDTO save(NabatGroupRequestDTO nabatGroupRequestDTO);
    List<NabatGroupResponseDTO> findAll();
    List<NabatGroupResponseDTO> findAllByUnitId(UUID unitId);
}
