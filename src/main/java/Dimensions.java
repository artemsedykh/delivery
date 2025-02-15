public enum Dimensions {
    LARGE(200),
    SMALL(100);

    private final int dimensionCost;

    Dimensions(int dimensionCost) {
        this.dimensionCost = dimensionCost;
    }

    public int getDimensionsCost() {
        return dimensionCost;
    }
}
