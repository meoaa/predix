package project.predix.sales.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.exception.NotFoundStoreByMemberException;
import project.predix.member.domain.Member;
import project.predix.sales.domain.Sales;
import project.predix.sales.domain.SalesType;
import project.predix.sales.dto.SalesCreateRequestDto;
import project.predix.sales.repository.SalesRepository;
import project.predix.store.domain.entity.Store;
import project.predix.store.repository.StoreRepository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class SalesService {

    private final SalesRepository salesRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public List<SalesCreateRequestDto> salesDataSortedAndSave(
            List<SalesCreateRequestDto> dtos, Member member){
        Store store = storeRepository.findByMember(member)
                .orElseThrow(NotFoundStoreByMemberException::new);

        SalesType type = dtos.get(0).getType();

        long l = salesRepository.deleteByStoreIdAndType(store.getId(), type);
        System.out.println("l = " + l);

        AtomicInteger order = new AtomicInteger(1);
        List<Sales> salesEntities = dtos.stream()
                .sorted(Comparator.comparing(SalesCreateRequestDto::getStartDate))
                .peek(dto -> dto.setOrderNum(order.getAndIncrement()))
                .map(Sales::of)
                .peek(store::assignSales)
                .toList();

        salesRepository.saveAll(salesEntities);

        return dtos;
    }




}
