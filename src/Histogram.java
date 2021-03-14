import java.io.*;

class Histogram {

    // la valeur 0 est égale à la valeur minimale de niveaux de gris
    private int data[];
    private short minValue;

    public Histogram(GreyImage im)
    {

        minValue = im.getMin();

        data = new int[im.getMax()+1 - minValue];


        short tmpData[] = im.getData();
        for(short i : tmpData) {
            data[i-minValue] = (data[i-minValue]+1);
        }
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    void saveHisto(String filename) throws FileNotFoundException, IOException
    {
        FileOutputStream fileout=new FileOutputStream(filename);
        for(int i=0; i<data.length; i++)
        {
            String tmp=minValue+i + " " + data[i]+"\n";
            fileout.write(tmp.getBytes());
        }

        fileout.close();
    }

    public int getMin() {
        return minValue;
    }

    public int getPeak() {
        int pic = data[0];
        for(int i : data) {
            if(data[i] > pic) {
                pic = data[i];
            }
        }

        return pic;
    }

}