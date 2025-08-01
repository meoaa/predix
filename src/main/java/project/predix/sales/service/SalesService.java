package project.predix.sales.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.predix.exception.NotFoundSalesRecordException;
import project.predix.exception.NotFoundStoreByMemberException;
import project.predix.exception.PermissionDeniedException;
import project.predix.member.domain.Member;
import project.predix.sales.domain.Sales;
import project.predix.sales.domain.SalesType;
import project.predix.sales.dto.SalesChartDataDto;
import project.predix.sales.dto.SalesUpsertRequestDto;
import project.predix.sales.dto.SalesResponseDto;
import project.predix.sales.repository.SalesRepository;
import project.predix.store.domain.entity.Store;
import project.predix.store.repository.StoreRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class SalesService {

    private final SalesRepository salesRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public List<SalesResponseDto> salesDataSortedAndSave(
            List<SalesUpsertRequestDto> dtos, Member member){

        Store store = storeRepository.findByMember(member)
                .orElseThrow(NotFoundStoreByMemberException::new);

        List<Sales> newSalesList = new ArrayList<>();

        for(SalesUpsertRequestDto dto : dtos){
            if(dto.getId() == null){
                Sales newSales = Sales.of(dto);
                store.assignSales(newSales);
                newSalesList.add(newSales);
            }else{
                Sales existingSales = salesRepository.findById(dto.getId())
                        .orElseThrow(NotFoundSalesRecordException::new);
                existingSales.updateAmount(dto.getAmount());
            }
        }
        salesRepository.saveAll(newSalesList);

        List<Sales> finalSalesList = salesRepository.findAllByStoreIdAndTypeOrderByStartDate(store.getId(), dtos.get(0).getType());

        return finalSalesList.stream()
                .map(SalesResponseDto::of)
                .toList();
    }

    public List<SalesResponseDto> getSalesDataDtos(long storeId, SalesType type){
         return salesRepository.findAllByStoreIdAndTypeOrderByStartDate(storeId, type).stream()
                .map(SalesResponseDto::of)
                .collect(Collectors.toList());
    }

    public Map<SalesType, SalesChartDataDto> getSalesDataForChart(long storeId) {
        List<Sales> foundSales = salesRepository.findAllByStoreIdOrderByStartDate(storeId);

        return foundSales.stream()
                .map(SalesResponseDto::of) // Sales 엔티티의 label 필드를 포함하여 SalesResponseDto로 변환
                .collect(Collectors.groupingBy(
                        SalesResponseDto::getType, // SalesType으로 그룹화
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    List<String> labels = list.stream()
                                            .map(SalesResponseDto::getLabel)
                                            .collect(Collectors.toList());
                                    List<Long> amounts = list.stream()
                                            .map(SalesResponseDto::getAmount)
                                            .collect(Collectors.toList());

                                    // --- 이 부분이 변경됩니다! ---
                                    String title = "";
                                    if (!list.isEmpty()) {
                                        SalesType type = list.get(0).getType(); // 현재 그룹의 SalesType 가져오기
                                        if (type == SalesType.MONTH) {
                                            title = "월별 매출"; // MONTH일 경우 "월간 매출"
                                        } else if (type == SalesType.WEEK) {
                                            title = "주별 매출"; // WEEK일 경우 "주간 매출"
                                        } else {
                                            title = type.name() + " 매출"; // 혹시 모를 다른 타입 대비
                                        }
                                    }
                                    // --- 변경 끝 ---

                                    return SalesChartDataDto.builder()
                                            .labels(labels)
                                            .data(amounts)
                                            .title(title) // 설정된 한글 제목 사용
                                            .build();
                                }
                        )
                ));
    }

    @Transactional
    public long deleteSales(long storeId, Member member){
        Store foundStore = storeRepository.findByMember(member)
                .orElseThrow(NotFoundStoreByMemberException::new);

        Sales foundSales = salesRepository.findById(storeId)
                .orElseThrow(NotFoundSalesRecordException::new);

        if(!foundSales.getStore().equals(foundStore)){
            throw new PermissionDeniedException();
        }
        salesRepository.delete(foundSales);
        return storeId;
    }

    public Path exportCsv(long storeId, SalesType type) throws IOException{
        Path tmp = Files.createTempFile("sales_", ".csv");
        List<Sales> rows = salesRepository.findAllByStoreIdAndTypeOrderByStartDate(storeId, type);
        for(Sales s : rows){
            String line = s.getStartDate() + "," + s.getAmount() + "\n";
            Files.writeString(tmp,line, StandardOpenOption.APPEND);
        }
        return tmp;
    }


}
