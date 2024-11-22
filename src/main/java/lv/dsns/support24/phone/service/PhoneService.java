package lv.dsns.support24.phone.service;

import lv.dsns.support24.phone.controller.dto.response.PhoneResponseDTO;
import lv.dsns.support24.phone.service.filter.PhoneFilter;

import java.util.List;

public interface PhoneService {
    List<PhoneResponseDTO> findAll(PhoneFilter filter);
}
