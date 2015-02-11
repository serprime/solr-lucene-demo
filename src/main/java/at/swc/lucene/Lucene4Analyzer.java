package at.swc.lucene;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * @author steinwenderp
 */
public final class Lucene4Analyzer {

    private static final String[] examples = {
            "The quick brown fox jumped over the lazy dog",
            "XY&Z Corporation - xyz@example.com",
            "i'll email you at xyz@example.com"
    };

    private static final Analyzer[] analyzers = new Analyzer[]{
            new WhitespaceAnalyzer(),
            new SimpleAnalyzer(),
            new StopAnalyzer(),
            new StandardAnalyzer(),
            new ClassicAnalyzer(),
            new EdgeNGramAnalyzer()
    };

    public static void main(String[] args) throws IOException {
        for (String example : examples) {
            analyze(example);
        }

    }

    private static void analyze(String text) throws IOException {
        System.out.println("Analyzing \"" + text + "\"");
        for (Analyzer analyzer : analyzers) {
            String name = analyzer.getClass().getSimpleName();
            System.out.println("\n" + name + ":");
            displayTokens(analyzer, text);
        }
    }

    private static void displayTokens(Analyzer analyzer, String text) throws IOException {
        displayTokens(analyzer.tokenStream("contents", new StringReader(text)));
    }

    private static void displayTokens(TokenStream stream) throws IOException {
        CharTermAttribute termAttribute = stream.addAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = stream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionAttribute = stream.addAttribute(PositionIncrementAttribute.class);
        TypeAttribute typeAttribute = stream.addAttribute(TypeAttribute.class);

        stream.reset();

        int position = 0;
        while (stream.incrementToken()) {
            int positionIncrement = positionAttribute.getPositionIncrement();
            position += positionIncrement;

            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String type = typeAttribute.type();
            String term = termAttribute.toString();
            System.out.format("%2d: %-20s %3d->%3d %s\n", position, "[" + term + "]", startOffset, endOffset, type);
        }
        System.out.println("");
        stream.close();
    }

    private static class EdgeNGramAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
            Tokenizer source = new ClassicTokenizer(reader);
            TokenStream filter = new LowerCaseFilter(source);
            filter = new EdgeNGramTokenFilter(filter, 1, 10);
            return new TokenStreamComponents(source, filter);
        }
    }
}
