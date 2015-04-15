/*
package com.example.akash.fourthestatesample;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.*;

*/
/**
 * Created by akash on 11/4/15.
 *//*

public class NounsCollector {
    public static Set<String> nounPhrases = new HashSet<>();
    static Set<String> adjectivePhrases = new HashSet<>();
    static Set<String> verbPhrases = new HashSet<>();
    InputStream is=null;

    */
/*private static String line = "The Moon is a barren, rocky world     without air and water. It has dark lava plain on its surface. " +
            "The Moon is filled wit craters. It has no light of its own. It gets its light from the Sun. The Moo keeps changing its " +
            "shape as it moves round the Earth. It spins on its axis in 27.3 days stars were named after the Edwin Aldrin were the " +
            first ones to set their foot on the Moon on 21 July 1969 They reached the Moon in their space craft named Apollo II";*//*

    String line = "Prime Minister Narendra Modi on Saturday visited Airbus's facility in Toulouse, in southern France, and called it amazing";

    public void getNounPhrases(Parse p) {
        if (p.getType().equals("NN") || p.getType().equals("NNS") || p.getType().equals("NNP") || p.getType().equals("NNPS")) {
            nounPhrases.add(p.getCoveredText());
        }

        if (p.getType().equals("JJ") || p.getType().equals("JJR") || p.getType().equals("JJS")) {
            adjectivePhrases.add(p.getCoveredText());
        }

        if (p.getType().equals("VB") || p.getType().equals("VBP") || p.getType().equals("VBG") || p.getType().equals("VBD") || p.getType().equals("VBN")) {
            verbPhrases.add(p.getCoveredText());
        }

        for (Parse child : p.getChildren()) {
            getNounPhrases(child);
        }
    }

    public String[] getNouns() {
        return (String[]) nounPhrases.toArray(new String[nounPhrases.size()]);
    }

    public void parserAction(String line) {
        Context context = FirebaseApp.getContext();
        Log.d("test12", " noun parsering ");

            ParserModel model = null;
        try {
            is =context.getAssets().open("en_parser_chunking.bin");
            Log.d("test12","input stream "+is);

            model = new ParserModel(is);
        } catch (Exception e) {
            Log.d("test12","error:"+e.getMessage());
            e.printStackTrace();
        }
        opennlp.tools.parser.Parser parser = ParserFactory.create(model);
        Parse topParses[] = ParserTool.parseLine(line, parser, 1);
        Log.d("test12"," perser size:"+topParses.length);
        for (Parse p : topParses) {
            //p.show();
            getNounPhrases(p);
        }
    }

  */
/*  public static void main(String[] args) throws Exception {
        new NounsCollector().parserAction();
        System.out.println(" original string "+new NounsCollector().line);
        System.out.println("List of Noun Parse : "+nounPhrases);
        System.out.println("List of Adjective Parse : "+adjectivePhrases);
        System.out.println("List of Verb Parse : "+verbPhrases);
    }*//*

}

*/
