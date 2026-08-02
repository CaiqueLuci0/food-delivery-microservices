package food.delivery.order_ms.infra.adapters.inbound.web.presenter.dto.ordercontroller.get;

import food.delivery.order_ms.core.domain.entities.Order;
import food.delivery.order_ms.core.domain.entities.ProductSnapshot;
import food.delivery.order_ms.core.domain.entities.SpecOptionSnapshot;

import java.util.ArrayList;
import java.util.List;

public final class OrderResponseMapper {

    private OrderResponseMapper() {
    }

    public static OrderResponseDto toResponse(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setScore(order.getScore());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setRestaurantOwnerId(order.getRestaurantOwnerId());
        dto.setClientId(order.getClientId());

        List<ProductSnapshotResponseDto> snapshots = new ArrayList<>();
        if (order.getProductSnapshots() != null) {
            for (ProductSnapshot snapshot : order.getProductSnapshots()) {
                snapshots.add(toProductSnapshotResponse(snapshot));
            }
        }
        dto.setProductSnapshots(snapshots);
        return dto;
    }

    public static List<OrderResponseDto> toResponseList(List<Order> orders) {
        if (orders == null) {
            return List.of();
        }
        return orders.stream().map(OrderResponseMapper::toResponse).toList();
    }

    private static ProductSnapshotResponseDto toProductSnapshotResponse(ProductSnapshot snapshot) {
        ProductSnapshotResponseDto dto = new ProductSnapshotResponseDto();
        dto.setId(snapshot.getId());
        dto.setName(snapshot.getName());
        dto.setPrice(snapshot.getPrice());
        dto.setDescription(snapshot.getDescription());

        List<SpecOptionSnapshotResponseDto> options = new ArrayList<>();
        if (snapshot.getSpecOptionSnapshots() != null) {
            for (SpecOptionSnapshot option : snapshot.getSpecOptionSnapshots()) {
                SpecOptionSnapshotResponseDto optionDto = new SpecOptionSnapshotResponseDto();
                optionDto.setId(option.getId());
                optionDto.setName(option.getName());
                optionDto.setDescription(option.getDescription());
                optionDto.setExtraPrice(option.getExtraPrice());
                options.add(optionDto);
            }
        }
        dto.setSpecOptionSnapshots(options);
        return dto;
    }
}
