package com.textocat.lemma.predictor.utils.csv;

/**
 * Created by Денис on 30.04.2016.
 */

import com.textocat.lemma.predictor.utils.csv.raw.StatisticalRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVWriter {

    CSVPrinter printer;
    CSVFormat csvFormat;

    public CSVWriter() {
    }

    public void writeToCSV(String[] header, Collection<StatisticalRecord> records, File file, Double accuracy) throws IOException {
        csvFormat = CSVFormat.EXCEL.withHeader(header).withDelimiter(',').withEscape('"').withQuoteMode(QuoteMode.NONE);
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
        printer.println();
        accuracy = accuracy * 100;
        printer.print("Accuracy: " + accuracy.toString() + " %");
        printer.close();
    }


}