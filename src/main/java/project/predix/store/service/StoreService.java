package project.predix.store.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.member.domain.Member;
import project.predix.store.domain.entity.Store;
import project.predix.store.dto.CreateRequestDto;
import project.predix.store.dto.CreateResponseDto;
import project.predix.store.repository.StoreRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public CreateResponseDto saveStore(CreateRequestDto requestDto, Member member){
        Store store = Store.of(requestDto);
        member.assignStore(store);
        Store savedStore = storeRepository.save(store);
        return new CreateResponseDto(savedStore.getName(),savedStore.getAddress(),savedStore.getSince());
    }

}
