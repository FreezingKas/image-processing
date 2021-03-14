public class TestgreyImage {

    public static void main(String[] args) throws Exception {
        // TEST ERODE

        Mask M = Mask.makeBall1();

        GreyImage im = GreyImage.loadPGM("test1.pgm");

        GreyImage imErode = im.erode(M);

        imErode.savePGM("test1_erode.pgm");




    }


}
