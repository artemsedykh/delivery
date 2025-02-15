public class Delivery {
    private final double distance;
    private final Dimensions dimensions;
    private final boolean isFragile;
    private final Workload workload;

    private static final int MIN_DELIVERY_COST = 400;

    public Delivery(double distance, Dimensions dimensions, boolean isFragile, Workload workload) {
        this.distance = distance;
        this.dimensions = dimensions;
        this.isFragile = isFragile;
        this.workload = workload;
    }

    private int getFragileCost() {
        return isFragile ? 300 : 0;
    }

    private int getDistanceCost() {
        if (distance > 30) return 300;
        if (distance > 10) return 200;
        if (distance > 2) return 100;
        if (distance >= 0) return 50;
        throw new IllegalArgumentException("Distance should be non-negative");
    }

    public double getDeliveryCost() {
        if (isFragile && distance > 30) {
            throw new IllegalArgumentException("Fragile items cannot be delivered beyond 30 km.");
        }
        double cost = (getDistanceCost() + getFragileCost() + this.dimensions.getDimensionsCost()) * this.workload.getWorkloadRate();
        return Math.max(cost, MIN_DELIVERY_COST);
    }
}