import java.util.Arrays;

public class TestgreyImage {

    public static void main(String[] args) throws Exception {

        for (String arg : args) {
            System.out.println(arg);
            GreyImage im = GreyImage.loadPGM("imgs/" + arg + ".pgm");

            // TEST ERODE
            Mask M1 = Mask.makeBall1();

            GreyImage imErode_B1 = im.erode(M1);

            imErode_B1.savePGM("tests/" + arg + "_erode_B1.pgm");

            // TEST DILATE
            GreyImage imDilate_B1 = im.dilate(M1);

            imDilate_B1.savePGM("tests/" + arg + "_dilate_B1.pgm");


            Mask M2 = Mask.makeBall2();

            GreyImage imErode_B2 = im.erode(M2);

            imErode_B2.savePGM("tests/" + arg + "_erode_B2.pgm");

            // TEST DILATE
            GreyImage imDilate_B2 = im.dilate(M2);

            imDilate_B2.savePGM("tests/" + arg + "_dilate_B2.pgm");


            // TEST OPEN
            GreyImage imOpen = im.open(M1);

            imOpen.savePGM("tests/" + arg + "_open.pgm");

            // TEST CLOSQE
            GreyImage imClose = im.close(M1);

            imOpen.savePGM("tests/" + arg + "_close.pgm");

            // TEST GRADIENT MORPHOLOGIQUE

            GreyImage imGradient = im.morphologicalGradient();

            imGradient.savePGM("tests/" + arg + "_morpho.pgm");

        }

        Mask M2 = Mask.makeBall2();

        GreyImage im = GreyImage.loadPGM("tests/bloodcells.pgm");

        im.seuiller((short)60);
        im.negative();

        im.savePGM("tests/bloodcells_seuil_60.pgm");

        GreyImage imErode = im.erode(M2);

        imErode.savePGM("tests/bloodcells_erode.pgm");

        GreyImage imDilate = im.dilate(M2);

        imDilate.savePGM("tests/bloodcells_dilate.pgm");



    }


}
