package csv;

import com.textocat.lemma.predictor.utils.csv.CSVWriter;
import com.textocat.lemma.predictor.utils.csv.raw.StatisticalRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Денис on 30.04.2016.
 */
public class CSVWriterr {
    public static void main(String[] args) throws IOException {

        String[] header = {"Wordform", "Gold", "Predicted", "Status"};

        CSVFormat format = CSVFormat.EXCEL.withHeader(header);

        List<StatisticalRecord> statisticalRecords = new ArrayList<>();

        statisticalRecords.add(new StatisticalRecord("111", "11", "1", "0"));

        statisticalRecords.add(new StatisticalRecord("000", "00", "0", "1"));

        File f = new File("result.csv");

        CSVWriter writer = new CSVWriter();

        writer.writeToCSV(header, statisticalRecords, f);
    }
}