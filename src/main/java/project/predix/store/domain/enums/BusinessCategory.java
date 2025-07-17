package project.predix.store.domain.enums;

import lombok.Getter;

@Getter
public enum BusinessCategory {
    FOOD_KOREAN("한식 음식점"),
    FOOD_CHINESE("중식 음식점"),
    FOOD_JAPANESE("일식·초밥"),
    FOOD_SNACK_DELIVERY("분식·배달전문"),
    FOOD_CAFE("카페·음료"),
    FOOD_BAKERY("제과·베이커리"),
    FOOD_CHICKEN_PIZZA("치킨·피자"),
    FOOD_PUB("호프·주점"),
    ACCOM_GUESTHOUSE("게스트하우스·숙박"),

    // 도·소매업
    RETAIL_CONVENIENCE("편의점"),
    RETAIL_GROCERY("슈퍼·식료품"),
    RETAIL_FASHION("의류·패션"),
    RETAIL_BEAUTY("화장품·뷰티"),
    RETAIL_ELECTRONICS("전자·가전"),
    RETAIL_MISC("생활잡화"),
    RETAIL_BOOK("서점·문구"),

    // 서비스업
    SERVICE_BEAUTY_SALON("미용실·이용원"),
    SERVICE_LAUNDRY("세탁·수선"),
    SERVICE_EDU("교육·학원"),
    SERVICE_MEDICAL("병원·약국"),
    SERVICE_FITNESS("피트니스·헬스"),
    SERVICE_PHOTO("사진관·스튜디오"),
    SERVICE_PC_KARAOKE("PC방·노래방"),
    SERVICE_REAL_ESTATE("부동산중개"),
    SERVICE_CAR_REPAIR("자동차정비");

    private final String label;

    BusinessCategory(String label) {
        this.label = label;
    }
}
