package project.predix.sales.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.sales.domain.Sales;
import project.predix.sales.dto.SalesCreateRequestDto;
import project.predix.sales.repository.SalesRepository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class SalesService {

    private final SalesRepository salesRepository;

    @Transactional
    public List<SalesCreateRequestDto> salesDataSortedAndSave(
            List<SalesCreateRequestDto> requestDtos){

        List<SalesCreateRequestDto> sortedDtos = sortedAndAddNumToList(requestDtos);

        for (SalesCreateRequestDto dto : sortedDtos) {
            Sales sales = Sales.of(dto);
            salesRepository.save(sales);
        }

        return sortedDtos;

    }

    private static List<SalesCreateRequestDto> sortedAndAddNumToList(List<SalesCreateRequestDto> requestDtos) {
        AtomicInteger orderCounter = new AtomicInteger(1);
        return requestDtos.stream()
                .sorted(Comparator.comparing(SalesCreateRequestDto::getStartDate))
                .peek(dto -> dto.setOrderNum(orderCounter.getAndIncrement()))
                .toList();
    }
}
