package a11942924;

public class HonoraryPlayer extends Player {
    private static int count = 0;

    public HonoraryPlayer(final String name) {
        super(name);
        ++count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "[Ehrenmitglied]" + super.toString();
    }
}
