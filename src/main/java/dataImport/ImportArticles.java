package dataImport;

    /**
     * @autor https://github.com/manishkanadje/reuters-21578/blob/master/ExtractReuters.java
     * Copyright 2005 The Apache Software Foundation
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    /**
     * @autor Alicja Anszpergier na podstawie https://github.com/manishkanadje/reuters-21578/blob/master/ExtractReuters.java
     * Split the Reuters SGML documents into ArrayList containing: Title,
     * Date, Dateline, Body
     */
    public class ImportArticles {
        private File[] sgmFiles;
        private ArrayList<Article> articles;
        public ImportArticles(ArrayList<Article> articles) {
        this.articles=articles;
        }

        public void extract(Path fileDir) {
            File folder =new File(fileDir.toString());

            sgmFiles = folder.listFiles();
            if (sgmFiles != null && sgmFiles.length > 0) {
                for (File sgmFile : sgmFiles) {
                    extractFile(articles, sgmFile);
                }
            } else {
                System.err.println("No .sgm files in " + fileDir);
            }
        }

        /*
         * private static String[] META_CHARS = { "&", "<", ">", "\"", "'" };
         *
         * private static String[] META_CHARS_SERIALIZATIONS = { "&amp;", "&lt;",
         * "&gt;", "&quot;", "&apos;" };
         */

        /**
         * Wypakowuje każdy artykuł do klasy Article
         * @param articles: ArrayList<Article>
         */
        protected void extractFile(ArrayList<Article> articles, File sgmFile) {
            try {

                List<Pattern> patterns = new ArrayList<>();

                Pattern TITLE_PATTERN = Pattern.compile("((<TITLE>)(.*?)(</TITLE>))");
                patterns.add(TITLE_PATTERN);

                Pattern DATE_PATTERN = Pattern.compile("((<DATE>)(.*?)(</DATE>))");
                patterns.add(DATE_PATTERN);

                Pattern DATELINE_PATTERN = Pattern.compile("((<DATELINE>)(.*?)(</DATELINE>))");
                patterns.add(DATELINE_PATTERN);

                Pattern UNKNOWN_PATTERN = Pattern.compile("((<UNKNOWN>)(.*?)(</UNKNOWN>))");
                patterns.add(UNKNOWN_PATTERN);

                Pattern ORGS_PATTERN = Pattern.compile("((<ORGS>)(.*?)(</ORGS>))");
                patterns.add(ORGS_PATTERN);

                Pattern STEXTUNPROC_PATTERN = Pattern.compile("((<TEXT TYPE=\"UNPROC\">)(.*?))");
                patterns.add(STEXTUNPROC_PATTERN);
                Pattern FTEXTUNPROC_PATTERN = Pattern.compile("</TEXT>");
                patterns.add(FTEXTUNPROC_PATTERN);

                Pattern SBODY_PATTERN = Pattern.compile("((<BODY>)(.*?))");
                patterns.add(SBODY_PATTERN);
                Pattern FBODY_PATTERN = Pattern.compile("</BODY>");
                patterns.add(FBODY_PATTERN);

                Pattern TOPICS_PATTERN = Pattern.compile("((<TOPICS>)(.*?)(</TOPICS>))");
                patterns.add(TOPICS_PATTERN);

                Pattern PLACES_PATTERN = Pattern.compile("((<PLACES>)(.*?)(</PLACES>))");
                patterns.add(PLACES_PATTERN);

                Pattern PEOPLE_PATTERN = Pattern.compile("((<PEOPLE>)(.*?)(</PEOPLE>))");
                patterns.add(PEOPLE_PATTERN);

                Pattern EXCHANGES_PATTERN = Pattern.compile("((<EXCHANGES>)(.*?)(</EXCHANGES>))");
                patterns.add(EXCHANGES_PATTERN);

                Pattern COMPANIES_PATTERN = Pattern.compile("((<COMPANIES>)(.*?)(</COMPANIES>))");
                patterns.add(COMPANIES_PATTERN);

                Pattern D_PATTERN = Pattern.compile("((.*?)(<D>)(.*?)(</D>)(.*?))");

                BufferedReader reader = new BufferedReader(new FileReader(sgmFile));

                StringBuilder buffer = new StringBuilder(10024);
                //StringBuilder outBuffer = new StringBuilder(1024);

                String line = null;
                TreeMap<String,ArrayList<String>> articleTmpData = new TreeMap<>();
                int docNumber = 0;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    // when we see a closing reuters tag, flush the file

                    if (line.contains("</REUTERS")) {
                        // Replace the SGM escape sequences
                        Article article = new Article(articleTmpData);
                        articles.add(article);
                        buffer.append(line).append(' ');// accumulate the strings

                    }


                    if (line.contains("<REUTERS")) {
                            articleTmpData = new TreeMap<>();
                    } else {
                        buffer.append(line);
                        for (Pattern pattern : patterns) {
                            Matcher matcher = pattern.matcher(buffer);
                            if (matcher.find()) {
                                //for (int i = 1; i <= matcher.groupCount(); i++) {
                                    //if (matcher.group(i) != null) {
                                        String string = pattern.pattern();
                                        ArrayList<String> tmp = new ArrayList<>();
                                        String field = string.substring(
                                                string.indexOf("<") + 1,
                                                string.indexOf(">")).trim();
                                        if (field != null && !field.trim().isEmpty()) {
                                            Matcher mat = D_PATTERN.matcher(buffer);
                                            if (mat.find()) {
                                                tmp = new ArrayList<>();
                                                if (mat.group() != null) {
                                                    int p = mat.group().indexOf("D>") + 2;
                                                    int k = mat.group().lastIndexOf("</D");
                                                    if (p >= 0 && p < k && k < mat.group().length()) {
                                                        if (!(mat.group().substring(p, k).trim().isEmpty()) && !(mat.group().substring(p, k).trim() == null)) {
                                                            tmp.add(mat.group().substring(p, k).trim());
                                                        }
                                                    }
                                                }
                                                while(mat.find()) {
                                                    int m = mat.groupCount();
                                                    for (int j = 1; j < m - 1; j++) {
                                                        if (mat.group(j) != null) {
                                                            int p = mat.group(j).indexOf("D>") + 2;
                                                            int k = mat.group(j).lastIndexOf("</D");
                                                            if (p >= 0 && p < k && k < mat.group(j).length()) {
                                                                if (!(mat.group(j).substring(p, k).trim().isEmpty()) && !(mat.group(j).substring(p, k).trim() == null)) {
                                                                    tmp.add(mat.group(j).substring(p, k).trim());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                if (pattern == SBODY_PATTERN || pattern==STEXTUNPROC_PATTERN) {
                                                    String s = buffer.substring(buffer.lastIndexOf(">") + 1, buffer.length()).trim();
                                                    do {
                                                        line = reader.readLine();
                                                        if(line==null && !line.isEmpty()){
                                                            line=" ";}
                                                        s+=line+" ";
                                                    } while (!line.contains(FBODY_PATTERN.pattern()) || !line.contains(FTEXTUNPROC_PATTERN.pattern()));
                                                    tmp.add(s);
                                                } else {
                                                    int s = matcher.group().indexOf(">") + 1;
                                                    int u = matcher.group().lastIndexOf("</" + field);
                                                    if(u>=s) {
                                                        if (!(matcher.group().substring(s, u).trim() == null) && !(matcher.group().substring(s, u).trim().isEmpty())) {
                                                            tmp.add(matcher.group().substring(s, u).trim());
                                                        }
                                                    }

                                            }
                                            }
                                            if(!tmp.isEmpty()&&!(tmp.contains(null))){
                                                articleTmpData.put(field, tmp);
                                            }
                                        }
                                    //}
                                //}
                            }
                        }
                        count++;
                        buffer = new StringBuilder(10024);
                    }
                    buffer = new StringBuilder(10024);
                }
                reader.close();
                buffer.delete(0,buffer.length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }