package lv.dsns.support24.nabatmessage.controller;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.nabatmessage.controller.dto.request.NabatMessageRequestDTO;
import lv.dsns.support24.nabatmessage.controller.dto.response.NabatMessageResponseDTO;
import lv.dsns.support24.nabatmessage.service.NabatMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/nabat/messages")
@RequiredArgsConstructor
public class NabatMessageController {

    private final NabatMessageService nabatMessageService;

    @GetMapping
    public ResponseEntity<List<NabatMessageResponseDTO>> getAllNabatMessages() {

        List<NabatMessageResponseDTO> allNabatMessages = nabatMessageService.getAll();

        return ResponseEntity.ok(allNabatMessages);
    }

    @PostMapping
    public ResponseEntity<NabatMessageResponseDTO> saveNabatMessage(@RequestBody NabatMessageRequestDTO nabatMessageRequestDTO) {

        NabatMessageResponseDTO savedNabatMessage = nabatMessageService.save(nabatMessageRequestDTO);

        return ResponseEntity.ok(savedNabatMessage);
    }

    @DeleteMapping("/{nabatMessageId}")
    public ResponseEntity<Void> deleteNabatMessage(@PathVariable UUID nabatMessageId){
        nabatMessageService.delete(nabatMessageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
