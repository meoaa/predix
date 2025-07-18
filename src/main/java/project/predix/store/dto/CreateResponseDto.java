package project.predix.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResponseDto {
    private String storeName;
    private String storeAddress;
    private LocalDate since;


}
