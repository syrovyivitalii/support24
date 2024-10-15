package lv.dsns.support24.nabatgroup.service;

import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface NabatGroupService {
    NabatGroupResponseDTO save(Principal principal, NabatGroupRequestDTO nabatGroupRequestDTO);

    List<NabatGroupResponseDTO> findAll();

    List<NabatGroupResponseDTO> findAllByUnitId(UUID unitId);

    NabatGroupResponseDTO findById(UUID id);

    NabatGroupResponseDTO update(UUID id, NabatGroupRequestDTO nabatGroupRequestDTO);
}
