package Admin.entity.Factory;

public class TableDataType<S> {
    private final S s;

    public TableDataType(S s) {
        this.s = s;
    }

    public S getS() {
        return s;
    }

}
