/**
 * Copyright (c) 2014, Sindice Limited. All Rights Reserved.
 *
 * This file is part of the SIREn project.
 *
 * SIREn is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * SIREn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sindicetech.siren.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.Version;

import com.sindicetech.siren.analysis.filter.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Analyzer designed to deal with any kind of URIs and perform some
 * post-processing on URIs.
 * <p>
 * The URI normalisation can be configured using
 * {@link #setUriNormalisation(URINormalisation)}. You can disable it, activate
 * it only on URI local name, or on the full URI. However, URI normalisation on the
 * full URI is costly in term of CPU at indexing time, and can double the size
 * of the index, since each URI is duplicated by n tokens. By default, the URI
 * normalisation is disabled.
 *
 * @see URINormalisationFilter
 * @see URILocalnameFilter
 */
public class AnyURIAnalyzer extends Analyzer {

  private final CharArraySet stopSet;

  private final Version matchVersion;

  /** An unmodifiable set containing some common English words that are usually not
  useful for searching. */
  public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

  /** Types of URI normalisation */
  public enum URINormalisation {NONE, LOCALNAME, FULL};

  private URINormalisation normalisationType = URINormalisation.NONE;

  public AnyURIAnalyzer(final Version version) {
    this(version, STOP_WORDS_SET);
  }

  public AnyURIAnalyzer(final Version version, final CharArraySet stopWords) {
    stopSet = stopWords;
    matchVersion = version;
  }

  public AnyURIAnalyzer(final Version version, final String[] stopWords) {
    matchVersion = version;
    stopSet = StopFilter.makeStopSet(matchVersion, stopWords);
  }

  public AnyURIAnalyzer(final Version version, final File stopwords) throws IOException {
    this(version, new FileReader(stopwords));
  }

  public AnyURIAnalyzer(final Version version, final Reader stopWords) throws IOException {
    stopSet = WordlistLoader.getWordSet(stopWords, version);
    matchVersion = version;
  }

  public void setUriNormalisation(final URINormalisation n) {
    normalisationType = n;
  }

  @Override
  protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
    final WhitespaceTokenizer source = new WhitespaceTokenizer(matchVersion, reader);
    TokenStream sink = new URIDecodingFilter(source, "UTF-8");
    sink = this.applyURINormalisation(sink);
    sink = new MailtoFilter(sink);
    sink = new LowerCaseFilter(matchVersion, sink );
    sink = new StopFilter(matchVersion, sink, stopSet);
    sink = new LengthFilter(matchVersion, true, sink, 2, 256);
    return new TokenStreamComponents(source, sink);
  }

  /**
   * Given the type of URI normalisation, apply the right sequence of operations
   * and filters to the token stream.
   */
  private TokenStream applyURINormalisation(TokenStream in) {
    switch (normalisationType) {
      case NONE:
        return new URITrailingSlashFilter(in);

      // here, trailing slash filter is after localname filtering, in order to
      // avoid filtering subdirectory instead of localname
      case LOCALNAME:
        in = new URILocalnameFilter(in);
        return new URITrailingSlashFilter(in);

      // here, trailing slash filter is before localname filtering, in order to
      // avoid trailing slash checking on every tokens generated by the
      // URI normalisation filter
      case FULL:
        in = new URITrailingSlashFilter(in);
        return new URINormalisationFilter(in);

      default:
        throw new EnumConstantNotPresentException(URINormalisation.class,
          normalisationType.toString());
    }
  }

}
