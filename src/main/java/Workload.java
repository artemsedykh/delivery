public enum Workload {
    VERY_HIGH(1.6),
    HIGH(1.4),
    INCREASED(1.2),
    NORMAL(1);

    private final double workloadRate;

    Workload(double workloadRate){
        this.workloadRate = workloadRate;
    }

    public double getWorkloadRate(){
        return workloadRate;
    }
}
