package project.predix.store.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
import project.predix.weather.event.StoreRegisteredEvent;
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
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CreateResponseDto saveStore(CreateRequestDto dto, Long memberId){

        // 1) 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundStoreByMemberException::new);

        // 2) 주소(시·도, 시·군·구) → 기상청 지점 코드 매핑
        int stationCode = weatherStationMappingService.resolveCode(dto.getRegion1(), dto.getRegion2());
        dto.setStationCode(stationCode);              // DTO → Entity 변환에 사용

        // 3) Store 엔티티 생성 & 연관관계 설정
        Store store = Store.of(dto);                  // of() 내부에서 stationCode 세팅 필요
        member.assignStore(store);

        // 4) 저장
        Store saved = storeRepository.save(store);
        log.info("Store saved id={} stationCode={}", saved.getId(), stationCode);

        // 5) 날씨 데이터 백필 이벤트 발행 (비동기 처리)
        eventPublisher.publishEvent(new StoreRegisteredEvent(stationCode));

        return new CreateResponseDto(saved.getName(), saved.getAddress(), saved.getSince());}



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
