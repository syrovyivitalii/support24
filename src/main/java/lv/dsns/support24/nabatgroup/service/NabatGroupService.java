package lv.dsns.support24.nabatgroup.service;

import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;

public interface NabatGroupService {
    NabatGroupResponseDTO save(NabatGroupRequestDTO nabatGroupRequestDTO);
}
