package lv.dsns.support24.nabatmessage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.nabatmessage.controller.dto.request.NabatMessageRequestDTO;
import lv.dsns.support24.nabatmessage.controller.dto.response.NabatMessageResponseDTO;
import lv.dsns.support24.nabatmessage.mapper.NabatMessageMapper;
import lv.dsns.support24.nabatmessage.repository.NabatMessageRepository;
import lv.dsns.support24.nabatmessage.repository.entity.NabatMessage;
import lv.dsns.support24.nabatmessage.service.NabatMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NabatMessageServiceImpl implements NabatMessageService {

    private final NabatMessageMapper nabatMessageMapper;
    private final NabatMessageRepository nabatMessageRepository;

    @Override
    public List<NabatMessageResponseDTO> getAll(){
        List<NabatMessage> allNabatMessages = nabatMessageRepository.findAll();

        return allNabatMessages.stream().map(nabatMessageMapper ::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public NabatMessageResponseDTO save(NabatMessageRequestDTO nabatMessageRequestDTO) {
        if (existsByNabatMessage(nabatMessageRequestDTO.getNabatMessage())) {
            throw new ClientBackendException(ErrorCode.NABAT_MESSAGE_ALREADY_EXISTS);
        }

        var nabatMessage = nabatMessageMapper.mapToEntity(nabatMessageRequestDTO);
        var savedNabatMessage = nabatMessageRepository.save(nabatMessage);

        return nabatMessageMapper.mapToDTO(savedNabatMessage);
    }

    @Override
    public void delete(UUID nabatMessageId) {
        NabatMessage nabatMessage = nabatMessageRepository.findById(nabatMessageId)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.NABAT_MESSAGE_NOT_FOUND));

        nabatMessageRepository.delete(nabatMessage);
    }


    public boolean existsByNabatMessage(String nabatMessage) {

        return nabatMessageRepository.existsNabatMessageByNabatMessage(nabatMessage);
    }
}
