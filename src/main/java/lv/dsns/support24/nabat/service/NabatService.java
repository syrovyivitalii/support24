package lv.dsns.support24.nabat.service;

import lv.dsns.support24.nabat.controller.dto.request.NabatRequestDTO;
import lv.dsns.support24.nabat.controller.dto.response.NabatResponseDTO;

import java.util.List;
import java.util.UUID;

public interface NabatService {

    NabatResponseDTO save(NabatRequestDTO nabatRequestDTO);

    List<NabatResponseDTO> getAll();

    List<NabatResponseDTO> getAllByNabatGroup(UUID nabatGroupId);

    void delete(UUID nabatGroupId, UUID userId);
}
