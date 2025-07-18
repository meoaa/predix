package project.predix.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.store.domain.entity.Store;
import project.predix.store.dto.CreateRequestDto;
import project.predix.store.dto.CreateResponseDto;
import project.predix.store.repository.StoreRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public CreateResponseDto saveStore(CreateRequestDto requestDto){
        Store store = Store.of(requestDto);
        Store savedStore = storeRepository.save(store);
        return new CreateResponseDto(savedStore.getName(),savedStore.getAddress(),savedStore.getSince());
    }

}
