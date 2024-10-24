package lv.dsns.support24.phone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.phone.controller.dto.response.PhoneResponseDTO;
import lv.dsns.support24.phone.mapper.PhoneMapper;
import lv.dsns.support24.phone.repository.PhoneRepository;
import lv.dsns.support24.phone.repository.entity.Phone;
import lv.dsns.support24.phone.service.PhoneService;
import lv.dsns.support24.phone.service.filter.PhoneFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public List<PhoneResponseDTO> findAll(PhoneFilter filter) {
        List<Phone> allPhones = phoneRepository.findAll(getSearchSpecification(filter));

        return allPhones.stream().map(phoneMapper :: mapToDTO).collect(Collectors.toList());
    }

    private Specification<Phone> getSearchSpecification(PhoneFilter phoneFilter) {
        return Specification.where((Specification<Phone>) searchLikeString("phone", phoneFilter.getPhone()))
                .and((Specification<Phone>) searchFieldInCollectionOfIds("id", phoneFilter.getIds()))
                .and((Specification<Phone>) searchFieldInCollectionOfIds("userId", phoneFilter.getUserIds()))
                .and((Specification<Phone>) searchOnField("phoneType", phoneFilter.getPhoneType()));
    }
}
