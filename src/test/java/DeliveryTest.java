import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryTest {
    @Test
    @Tag("Positive")
    @DisplayName("Применение минимальной стоимости доставки")
    void testMinimalDeliveryConditionsCost() {
        Delivery delivery = new Delivery(0.1, Dimensions.SMALL, false, Workload.NORMAL);

        double expectedResult = 400;
        double actualResult = delivery.getDeliveryCost();

        assertEquals(expectedResult, actualResult, "Expected minimum delivery cost to be applied");
    }

    // https://habr.com/ru/articles/591007/

    static class DeliveryArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(20, Dimensions.LARGE, false, Workload.VERY_HIGH, 640),
                    Arguments.of(5, Dimensions.SMALL, true, Workload.HIGH, 700)
            );
        }
    }

    @ParameterizedTest(name = "Тест {index}: {0} км, Габариты: {1}, Хрупкий: {2}, Загруженность: {3} → Ожидаемая стоимость: {4}")
    @ArgumentsSource(DeliveryArgumentsProvider.class)
    @Tag("Positive")
    void testParametrizedCostCalculation(double distance, Dimensions dimensions, boolean isFragile, Workload workload, double expectedResult) {
        Delivery delivery = new Delivery(distance, dimensions, isFragile, workload);

        double actualResult = delivery.getDeliveryCost();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Расчёт стоимости доставки на расстояние более 30 км для нехрупкого груза")
    void testNotFragileItemBeyond30KmCost() {
        Delivery delivery = new Delivery(35, Dimensions.LARGE, false, Workload.INCREASED);

        double expectedResult = 600;
        double actualResult = delivery.getDeliveryCost();

        assertEquals(expectedResult, actualResult, "Expected cost to be calculated");
    }

    @Test
    @Tag("Negative")
    @DisplayName("Нельзя возить хрупкие грузы на расстояние более 30 км")
    void testFragileItemBeyond30KmCost() {
        Delivery delivery = new Delivery(35, Dimensions.SMALL, true, Workload.NORMAL);

        Exception e = assertThrows(IllegalArgumentException.class, delivery::getDeliveryCost,
                "Expected IllegalArgumentException");
        assertEquals(ErrorMessages.FRAGILE_ITEM_BEYOND_30_KM, e.getMessage());
    }

    @Test
    @Tag("Negative")
    @DisplayName("Отрицательное расстояние")
    void testNegativeDistanceCost() {
        Delivery delivery = new Delivery(-1, Dimensions.LARGE, true, Workload.NORMAL);

        Exception e = assertThrows(IllegalArgumentException.class, delivery::getDeliveryCost,
                "Expected IllegalArgumentException");
        assertEquals(ErrorMessages.NEGATIVE_DISTANCE, e.getMessage());
    }
}
