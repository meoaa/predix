package project.predix.store.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.exception.NotFoundStoreByMemberException;
import project.predix.member.domain.Member;
import project.predix.member.repository.MemberRepository;
import project.predix.store.domain.entity.Store;
import project.predix.store.dto.CreateRequestDto;
import project.predix.store.dto.CreateResponseDto;
import project.predix.store.dto.FindByMemberResponseDto;
import project.predix.store.repository.StoreRepository;
import project.predix.weather.weatherstation.service.WeatherStationMappingService;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final WeatherStationMappingService weatherStationMappingService;

    @Transactional
    public CreateResponseDto saveStore(CreateRequestDto requestDto, Long memberId){

        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(NotFoundStoreByMemberException::new);
        Store store = Store.of(requestDto);
        foundMember.assignStore(store);
        Store savedStore = storeRepository.save(store);

        int stnCode = weatherStationMappingService.resolveCode(savedStore.getRegion1(), savedStore.getRegion2());


        return new CreateResponseDto(savedStore.getName(),savedStore.getAddress(),savedStore.getSince());
    }



    public Optional<FindByMemberResponseDto> searchOptionalStoreByMember(Member member){
        return storeRepository.findByMember(member)
                .map(FindByMemberResponseDto::of);
    }

    public FindByMemberResponseDto searchStoreByMember(Member member){
        Store store = storeRepository.findByMember(member)
                .orElseThrow(NotFoundStoreByMemberException::new);
        return FindByMemberResponseDto.of(store);
    }

}
