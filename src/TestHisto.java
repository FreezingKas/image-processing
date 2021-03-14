import java.io.IOException;

public class TestHisto {

    public static void main(String[] args) throws Exception {
        GreyImage file = GreyImage.loadPGM("test.pgm");

        Histogram histogram = new Histogram(file);

        file.equalize(0,255);

        //histogram.saveHisto("histconstrast.txt");

        file.savePGM("Lenaqua.pgm");

    }
}
