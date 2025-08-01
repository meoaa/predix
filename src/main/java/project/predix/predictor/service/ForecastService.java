package project.predix.predictor.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.predix.predictor.dto.ForecastDto;
import project.predix.sales.domain.SalesType;
import project.predix.sales.service.SalesService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final SalesService salesService;

    /** <프로젝트 루트>/.venv/Scripts/python.exe */
    private static final String PYTHON =
            Paths.get(System.getProperty("user.dir"), ".venv", "Scripts", "python.exe").toString();

    /** <프로젝트 루트>/predictor/forecast.py */
    private static final String SCRIPT =
            Paths.get(System.getProperty("user.dir"), "predictor", "forecast.py").toString();

    /** LocalDate 역직렬화를 위해 JavaTimeModule 등록 */
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 매출 CSV를 Prophet 예측 모델에 넘겨 future 데이터를 받아온다.
     *
     * @param storeId 예측할 매장의 PK
     * @param type    WEEK(주) / MONTH(월)
     * @return        예측 결과 DTO 리스트
     */
    public List<ForecastDto> forecast(Long storeId, SalesType type)
            throws IOException, InterruptedException {

        /* Prophet 파라미터 */
        int periods = (type == SalesType.WEEK ? 4 : 2);      // 4주 or 2개월
        String freq = (type == SalesType.WEEK ? "W-MON" : "MS");

        /* 1) 과거 매출을 CSV 파일로 덤프 */
        Path csv = salesService.exportCsv(storeId, type);

        /* 2) 파이썬 프로세스 실행 */
        ProcessBuilder pb = new ProcessBuilder(
                PYTHON,
                SCRIPT,
                csv.toString(),
                String.valueOf(periods),
                freq
        );
        // stderr(경고·로그) 은 콘솔로 바로 흘려보냄
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process p = pb.start();
        //사용 후 삭제

        /* 3) 결과 수신 & 종료 코드 확인 */
        String json = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = p.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Python forecast failed (exit " + exitCode + ")");
        }
        Files.deleteIfExists(csv);
        /* 4) JSON → DTO */
        return mapper.readValue(json, new TypeReference<List<ForecastDto>>() {});
    }
}
