package lv.dsns.support24.phone.controller;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.phone.controller.dto.response.PhoneResponseDTO;
import lv.dsns.support24.phone.service.PhoneService;
import lv.dsns.support24.phone.service.filter.PhoneFilter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    @GetMapping("/public/phone")
    public ResponseEntity<List<PhoneResponseDTO>> getPhones(@ParameterObject PhoneFilter phoneFilter) {
        List<PhoneResponseDTO> all = phoneService.findAll(phoneFilter);

        return ResponseEntity.ok(all);
    }
}
