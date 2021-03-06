/*
 * Copyright (c) 2013-2018 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.graphaware.nlp.parser.pdf;

import com.graphaware.nlp.annotation.NLPModuleExtension;
import com.graphaware.nlp.parser.AbstractParser;
import com.graphaware.nlp.parser.domain.Page;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NLPModuleExtension(name = "parser.pdf.tika")
public class TikaPDFParser extends AbstractParser {

    private final PDFParser pdfParser = new PDFParser();

    @Override
    public List<Page> parse(InputStream fs, List<String> filterPatterns) throws Exception {
        List<Page> pages = new ArrayList<>();
        PageContentHandler handler = new PageContentHandler(filterPatterns);
        Metadata metadata = new Metadata();
        pdfParser.setSortByPosition(true);
        pdfParser.parse(fs, handler, metadata, new ParseContext());

        Map<Integer, List<String>> content =  handler.getImprovedContent();
        for (Integer i : content.keySet()) {
            Page page = new Page(i);
            for (String p : content.get(i)) {
                page.getParagraphs().add(p);
            }
            pages.add(page);
        }

        return pages;
    }
}
