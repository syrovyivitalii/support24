package lv.dsns.support24.nabatmessage.service;

import jakarta.transaction.Transactional;
import lv.dsns.support24.nabatmessage.controller.dto.request.NabatMessageRequestDTO;
import lv.dsns.support24.nabatmessage.controller.dto.response.NabatMessageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface NabatMessageService {

    List<NabatMessageResponseDTO> getAll();

    NabatMessageResponseDTO save(NabatMessageRequestDTO nabatMessageRequestDTO);

    void delete(UUID nabatMessageId);
}
