package lv.dsns.support24.phone.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.phone.controller.dto.enums.PhoneType;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequestDTO {
    private UserResponseDTO phoneUser;
    private String phone;
    private PhoneType phoneType;
}
