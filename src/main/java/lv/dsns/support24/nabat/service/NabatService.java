package lv.dsns.support24.nabat.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.nabat.controller.dto.request.NabatRequestDTO;
import lv.dsns.support24.nabat.controller.dto.response.NabatResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NabatService {

    NabatResponseDTO save(NabatRequestDTO nabatRequestDTO);

    List<NabatResponseDTO> saveList(Set<NabatRequestDTO> nabatRequestDTOs);

    List<NabatResponseDTO> getAll();

    PageResponse<NabatResponseDTO> getAllByNabatGroup(UUID nabatGroupId, Pageable pageable);

    void delete(UUID nabatGroupId, UUID userId);
}
