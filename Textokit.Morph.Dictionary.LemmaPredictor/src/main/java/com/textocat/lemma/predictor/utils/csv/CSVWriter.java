package com.textocat.lemma.predictor.utils.csv;

/**
 * Created by Денис on 30.04.2016.
 */

import com.textocat.lemma.predictor.utils.csv.raw.StatisticalRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVWriter {

    public CSVWriter() {
    }

    public void writeToCSV(String[] header, Collection<StatisticalRecord> records, File file) throws IOException {
        CSVFormat csvFormat = CSVFormat.EXCEL.withHeader(header);
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(new FileWriter(file), csvFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> raws = new ArrayList<>();
        ;
        for (StatisticalRecord record : records) {
            raws.add(record.getWordform());
            raws.add(record.getGold());
            raws.add(record.getPredicted());
            raws.add(record.getStatus());
            printer.printRecord(raws);
            printer.flush();
            raws.clear();
        }
        printer.close();
    }
}