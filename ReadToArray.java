import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class ReadToArray {

    public char[][] array; 
    //public File file; 

    public void readToArray (File file){
        try{
            FileReader file_reader = new FileReader(file);
            int ch; 
            int iterator = 0; 

            System.out.println(file + "\n"); 

            while ((ch = file_reader.read()) != -1){
                array[iterator][0] = (char)ch;
                System.out.println(ch + "\n"); 
            }
            file_reader.close();

            System.out.println(array.toString());

        } catch (Exception e){

        }
    } 
    
}
